package codesquad.server.bean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
    private static final BeanFactory INSTANCE = new BeanFactory();

    private final BeanStorage beanStorage;

    // TODO: Change Into Private
    public BeanFactory() {
        beanStorage = BeanStorage.getInstance();
        init();
    }

    public static BeanFactory getInstance() {
        return INSTANCE;
    }

    private void init() {
        try (InputStream inputStream = getClass()
            .getResourceAsStream("/bean_configuration.xml")) {
            if (inputStream == null) {
                throw new NoSuchFileException("File Not Found: /bean_configuration.xml");
            }
            NodeList nodeList = parseXMLDocument(inputStream);
            processBean(nodeList);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private NodeList parseXMLDocument(InputStream inputStream)
        throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();
        return document.getElementsByTagName("bean");
    }

    private void processBean(NodeList nodeList) {
        Queue<Class<?>> postProcessQueue = new LinkedList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element element = (Element) node;
            String className = element.getAttribute("class");
            try {
                generateBean(Class.forName(className), postProcessQueue);
            } catch (ClassNotFoundException e) {
                // TODO :Raise Exception
                log.error(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        postProcessing(postProcessQueue);
    }

    private void generateBean(Class<?> clazz, Queue<Class<?>> postProcessQueue) throws Exception {
        List<Object> beanList = new ArrayList<>();
        loop:
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            constructor.setAccessible(true);
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                Object parameterBean = beanStorage.getBean(parameterType);
                if (parameterBean == null) {
                    postProcessQueue.add(clazz);
                    break loop;
                }
                beanList.add(parameterBean);
            }
            beanStorage.addBean(clazz, constructor.newInstance(beanList.toArray()));
        }
    }

    private void postProcessing(Queue<Class<?>> queue) {
        try {
            while (!queue.isEmpty()) {
                Class<?> clazz = queue.poll();
                generateBean(clazz, queue);
                // TODO: 순환 참조 상황 핸들링하기
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public <T> T getBean(Class<T> clazz) {
        T bean = beanStorage.getBean(clazz);
        if (bean == null) {
            throw new IllegalArgumentException("Bean Not Found");
        }
        return bean;
    }

}
