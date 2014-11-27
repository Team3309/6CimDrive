
public class modifiedGyro {
  
  public initGyro() {
    result = new AccumulatorResult();
    if (m_analog == null) {
      System.out.println("Null m_analog");
    }
    m_voltsPerDegreePerSecond = kDefaultVoltsPerDegreePerSecond;
    m_analog.setAverageBits(kAverageBits);
    m_analog.setOversampleBits(kOversampleBits);
    double sampleRate = kSamplesPerSecond * (1 << (kAverageBits + kOversampleBits));
    m_analog.getModule().setSampleRate(sampleRate);

    Timer.delay(1.0);
    m_analog.initAccumulator();

    Timer.delay(kCalibrationSampleTime);

    m_analog.getAccumulatorOutput(result);

    int center = (int) ((double)result.value / (double)result.count + .5);

    m_offset = ((double)result.value / (double)result.count) - (double)center;

    m_analog.setAccumulatorCenter(center);

    m_analog.setAccumulatorDeadband(0); ///< TODO: compute / parameterize this
    m_analog.resetAccumulator();
  }
}
