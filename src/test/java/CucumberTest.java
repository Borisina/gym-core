import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",  // указываете путь к .feature файлам
        glue = "com.kolya.gym.steps"  // указываете пакет, где находятся определения шагов
)
public class CucumberTest {
    // Этот класс не содержит тестовых методов, потому что он служит "запускающим" для Cucumber
}
