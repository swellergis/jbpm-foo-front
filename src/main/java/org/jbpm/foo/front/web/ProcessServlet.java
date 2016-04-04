package org.jbpm.foo.front.web;

//import org.jbpm.examples.util.StartupBean;
//import org.jbpm.services.ejb.api.ProcessServiceEJBLocal;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;


@WebServlet(name = "process", urlPatterns = { "/process" })
public class ProcessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

//    @EJB
//    private ProcessServiceEJBLocal processService;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("doGet() from ProcessServlet");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        System.out.println("doPost() from ProcessServlet");
//        processRequest(request, response);

        String recipient = request.getParameter("recipient");

        long start = System.currentTimeMillis();
        String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
        String user = "kieserver";
        String password = "kieserver1!";

//        String containerId = "hr";
//        String processId = "hiring";
//        String containerId = "hello";
//        String processId = "hello";
        String containerId = "foo";
        String processId = "com.sample.rewards-basic";

        KieServicesConfiguration configuration = KieServicesFactory.newRestConfiguration(serverUrl, user, password);

        configuration.setMarshallingFormat(MarshallingFormat.JAXB);
//        configuration.addJaxbClasses(extraClasses);
//        KieServicesClient kieServicesClient =  KieServicesFactory.newKieServicesClient(configuration, kieContainer.getClassLoader());
        KieServicesClient kieServicesClient =  KieServicesFactory.newKieServicesClient(configuration);
        
        boolean deployContainer = true;
        KieContainerResourceList containers = kieServicesClient.listContainers().getResult();
        // check if the container is not yet deployed, if not deploy it
        if (containers != null) {
            for (KieContainerResource kieContainerResource : containers.getContainers()) {
                if (kieContainerResource.getContainerId().equals(containerId)) {
                    System.out.println("\t######### Found container " + containerId + " skipping deployment...");
                    deployContainer = false;
                    break;
                }
            }
        }
        // deploy container if not there yet        
        if (deployContainer) {
            System.out.println("\t######### Deploying container " + containerId);
//            KieContainerResource resource = new KieContainerResource(containerId, new ReleaseId("org.jbpm", "HR", "1.0"));
            KieContainerResource resource = new KieContainerResource(containerId, new ReleaseId("org.jbpm.foo.back", "jbpm-foo-back", "1.0"));
//            KieContainerResource resource = new KieContainerResource(containerId, new ReleaseId("org.mastertheboss.kieserver", "hello-kie-server", "1.0"));
            kieServicesClient.createContainer(containerId, resource);
        }
        // query for all available process definitions
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        List<ProcessDefinition> processes = queryClient.findProcesses(0, 10);
        System.out.println("\t######### Available processes" + processes);

        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        // get details of process definition
        ProcessDefinition definition = processClient.getProcessDefinition(containerId, processId);
        System.out.println("\t######### Definition details: " + definition);

        // start process instance
        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("name", "john");
//        params.put("age", 25);
        params.put("recipient", recipient);
        Long processInstanceId = processClient.startProcess(containerId, processId, params);
        System.out.println("\t######### Process instance id: " + processInstanceId);

//        long processInstanceId = -1;
//        try {
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("recipient", recipient);
//            processInstanceId = processService.startProcess(StartupBean.DEPLOYMENT_ID,
//                    "org.jbpm.examples.rewards", params);
//            System.out.println("Process instance " + processInstanceId + " has been successfully started.");
//        } catch (Exception e) {
//            throw new ServletException(e);
//        }
//
//        req.setAttribute("message", "process instance (id = "
//                + processInstanceId + ") has been started.");
//
//        ServletContext context = this.getServletContext();
//        RequestDispatcher dispatcher = context
//                .getRequestDispatcher("/index.jsp");
//        dispatcher.forward(req, res);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet temp</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet temp at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}