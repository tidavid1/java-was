package codesquad.server.bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.server.statics.StaticFileStorage;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BeanFactory는")
class BeanFactoryTest {

    private BeanStorage beanStorage;
    private BeanFactory beanFactory;

    @BeforeEach
    void init() {
        try {
            for (Constructor<?> constructor : BeanStorage.class.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                beanStorage = (BeanStorage) constructor.newInstance();
            }
            for (Constructor<?> constructor : BeanFactory.class.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                beanFactory = (BeanFactory) constructor.newInstance(beanStorage);
            }
        } catch (Exception ignore) {

        }
    }

    @Test
    @DisplayName("싱글턴을 지원한다.")
    void singleton() {
        // Act
        BeanFactory actualResult1 = BeanFactory.getInstance();
        BeanFactory actualResult2 = BeanFactory.getInstance();
        // Assert
        assertThat(actualResult1).isEqualTo(actualResult2);
    }

    @Test
    @DisplayName("Bean을 생성해서 BeanStorage에 저장한다.")
    void initTest() {
        // Act
        assertThat(beanStorage).extracting("map").satisfies(map -> assertThat(map).isNotNull());
    }

    @Test
    @DisplayName("저장된 Bean을 제공한다.")
    void getBean() {
        // Arrange
        Class<StaticFileStorage> actualClass = StaticFileStorage.class;
        // Act
        StaticFileStorage actualResult = beanFactory.getBean(actualClass);
        // Assert
        assertThat(actualResult).isInstanceOf(actualClass);
    }

    @Test
    @DisplayName("저장되지 않은 Bean을 요청하면 예외를 호출한다.")
    void getBeanWhenBeanNotStored() {
        // Arrange
        Class<BeanFactory> actualClass = BeanFactory.class;
        // Act & Assert
        assertThatThrownBy(() -> beanFactory.getBean(actualClass))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Bean Not Found");
    }

}