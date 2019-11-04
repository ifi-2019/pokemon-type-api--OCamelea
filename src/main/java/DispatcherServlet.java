import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<String, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Getting request for " + req.getRequestURI());
        // TODO 
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // on enregistre notre controller au démarrage de la servlet
        this.registerController(HelloController.class);
    }

    protected void registerController(Class controllerClass){
        if (controllerClass.isAnnotationPresent(Controller.class)) {
            Method[] methods = controllerClass.getMethods();
            if (methods.length != 0) {
                for (int i = 0; i < methods.length; i++) {
                    registerMethod(methods[i]);
                }
            }
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    protected void registerMethod(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            String uri = method.getAnnotation(RequestMapping.class).uri();
            if (uri != null && (method.getReturnType().equals(Void.TYPE)) == false) {
                this.uriMappings.put(uri, method);

                System.out.println("Registering method " + method.getName());
            }
        }
    }

    protected Map<String, Method> getMappings(){
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri){
        return this.uriMappings.get(uri);
    }
}