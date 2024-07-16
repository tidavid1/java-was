package codesquad.server.bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BeanStorage는")
class BeanStorageTest {

    private BeanStorage beanStorage;

    @BeforeEach
    void init() {
        try {
            for (Constructor<?> constructor : BeanStorage.class.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                beanStorage = (BeanStorage) constructor.newInstance();
            }
        } catch (Exception ignore) {

        }
    }

    @Test
    @DisplayName("싱글톤 패턴을 따른다")
    void singleton() {
        // Act
        BeanStorage storage = BeanStorage.getInstance();
        BeanStorage storage2 = BeanStorage.getInstance();
        // Assert
        assertThat(storage).isEqualTo(storage2);
    }

    @Test
    @DisplayName("Bean을 추가할 수 있다.")
    void addBean() {
        // Arrange
        Class<String> exceptedClazz = String.class;
        String exceptedBean = "Hello, Bean!";
        // Act
        beanStorage.addBean(exceptedClazz, exceptedBean);
        // Assert
        assertThat(beanStorage.getBean(exceptedClazz)).isEqualTo(exceptedBean);
    }

    @Test
    @DisplayName("이미 추가된 Bean을 추가하려고 하면 예외를 던진다.")
    void addBeanWithAlreadyCreatedBean() {
        // Arrange
        Class<String> clazz = String.class;
        String bean = "Hello, Bean!";
        beanStorage.addBean(clazz, bean);
        // Act & Assert
        assertThatThrownBy(() -> beanStorage.addBean(clazz, bean))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Bean Already Created");
    }

    @Test
    @DisplayName("Bean을 가져올 수 있다.")
    void getBean() {
        // Arrange
        Class<String> clazz = String.class;
        String bean = "Hello, Bean!";
        beanStorage.addBean(clazz, bean);
        // Act
        String actual = beanStorage.getBean(clazz);
        // Assert
        assertThat(actual).isEqualTo(bean);
    }

    @Test
    @DisplayName("존재하지 않는 Bean을 가져오면 null을 반환한다.")
    void getBeanWithNotExistsBean() {
        // Arrange
        Class<String> clazz = String.class;
        // Act
        String actual = beanStorage.getBean(clazz);
        // Assert
        assertThat(actual).isNull();
    }
}