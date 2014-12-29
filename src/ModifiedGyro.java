/**
 * Just like a regular gyro, except it can return angular velocity 
 * and scale the output of getAngle()
 */
import edu.wpi.first.wpilibj.AccumulatorResult;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Timer;


public class ModifiedGyro {

    static final int kOversampleBits = 10;
    static final int kAverageBits = 0;
    static final double kSamplesPerSecond = 50.0;
    static final double kCalibrationSampleTime = 5.0;
    static final double kDefaultVoltsPerDegreePerSecond = 0.007;
    AnalogChannel m_analog;
    double m_voltsPerDegreePerSecond;
    double m_offset;
    boolean m_channelAllocated;
    AccumulatorResult result;
  //can use m_voltsPerDefreePerSecond to scale and set a sensitevity, none is used right now so it will juse equal the default (.007)

    public ModifiedGyro(int port) {
        m_analog = new AnalogChannel(port);
        initGyro();
    }

    private void initGyro() {
        result = new AccumulatorResult();

        m_voltsPerDegreePerSecond = kDefaultVoltsPerDegreePerSecond;
        m_analog.setAverageBits(kAverageBits);
        m_analog.setOversampleBits(kOversampleBits);
        double sampleRate = kSamplesPerSecond * (1 << (kAverageBits + kOversampleBits));
        m_analog.getModule().setSampleRate(sampleRate);

        Timer.delay(1.0);
        m_analog.initAccumulator();

        Timer.delay(kCalibrationSampleTime);

        m_analog.getAccumulatorOutput(result);

        int center = (int) ((double) result.value / (double) result.count + .5);

        m_offset = ((double) result.value / (double) result.count) - (double) center;

        m_analog.setAccumulatorCenter(center);

        m_analog.setAccumulatorDeadband(0); ///< TODO: compute / parameterize this
        m_analog.resetAccumulator();
    }

    public double getAngle() {
        if (m_analog == null) {
            return 0.0;
        } else {
            m_analog.getAccumulatorOutput(result);

            long value = result.value - (long) (result.count * m_offset);

            double scaledValue = value * 1e-9 * m_analog.getLSBWeight() * (1 << m_analog.getAverageBits())
                    / (m_analog.getModule().getSampleRate() * m_voltsPerDegreePerSecond);

            return scaledValue;
        }
    }

    public double getAngularRateOfChange() {
        double rate = (m_analog.getVoltage() - m_offset) / m_voltsPerDegreePerSecond;
        return rate;
    }
    
    //called when drift becomes too much
    public void reset() {
        m_offset = m_analog.getVoltage();
        m_analog.resetAccumulator();
    }

}
