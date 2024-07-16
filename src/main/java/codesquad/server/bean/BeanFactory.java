package codesquad.server.bean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BeanFactory {

    private static BeanFactory instance;

    private final BeanStorage beanStorage;

    private BeanFactory() {
        beanStorage = BeanStorage.getInstance();
        init();
    }

    public static BeanFactory getInstance() {
        if (instance == null) {
            instance = new BeanFactory();
        }
        return instance;
    }

    private void init() {
        try (InputStream inputStream = getClass()
            .getResourceAsStream("/bean_configuration.xml")) {
            if (inputStream == null) {
                throw new NoSuchFileException("File Not Found: /bean_configuration.xml");
            }
            NodeList nodeList = parseXMLDocument(inputStream);
            processBean(nodeList);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    private NodeList parseXMLDocument(InputStream inputStream)
        throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();
        return document.getElementsByTagName("bean");
    }

    private void processBean(NodeList nodeList)
        throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Queue<Class<?>> postProcessQueue = new LinkedList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element element = (Element) node;
            String className = element.getAttribute("class");
            generateBean(Class.forName(className), postProcessQueue);
        }
        postProcessing(postProcessQueue);
    }

    private void generateBean(Class<?> clazz, Queue<Class<?>> postProcessQueue)
        throws InvocationTargetException, InstantiationException, IllegalAccessException {
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

    private void postProcessing(Queue<Class<?>> queue)
        throws InvocationTargetException, InstantiationException, IllegalAccessException {
        int queueSize = queue.size();
        int count = 0;
        while (!queue.isEmpty()) {
            Class<?> clazz = queue.poll();
            generateBean(clazz, queue);
            if (queueSize == queue.size()) {
                count += 1;
                if (count == queueSize) {
                    throw new IllegalArgumentException(
                        "Circular Reference Raised!! Check your Bean Configuration");
                }
                continue;
            }
            queueSize = queue.size();
            count = 0;
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
