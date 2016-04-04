package org.jbpm.foo.front.web;

//import org.jbpm.examples.util.StartupBean;
//import org.jbpm.services.ejb.api.RuntimeDataServiceEJBLocal;
//import org.jbpm.services.ejb.api.UserTaskServiceEJBLocal;
//import org.jbpm.services.task.commands.CompleteTaskCommand;
//import org.jbpm.services.task.commands.CompositeCommand;
//import org.jbpm.services.task.commands.StartTaskCommand;
//import org.kie.api.task.model.TaskSummary;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import org.kie.api.task.model.TaskSummary;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;

@WebServlet(name = "task", urlPatterns = { "/task" })
public class TaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

//    @EJB
//    private UserTaskServiceEJBLocal userTaskService;
//
//    @EJB
//    private RuntimeDataServiceEJBLocal runtimeDataService;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("doGet() from TaskServlet");
//        processRequest(request, response);

        String cmd = request.getParameter("cmd");
        String user = request.getParameter("user");
        System.out.println("");

        String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
        String userServer = "kieserver";
        String passwordServer = "kieserver1!";
        KieServicesConfiguration configuration = KieServicesFactory.newRestConfiguration(serverUrl, userServer, passwordServer);
        configuration.setMarshallingFormat(MarshallingFormat.JAXB);
//        configuration.addJaxbClasses(extraClasses);
//        KieServicesClient kieServicesClient =  KieServicesFactory.newKieServicesClient(configuration, kieContainer.getClassLoader());
        KieServicesClient kieServicesClient =  KieServicesFactory.newKieServicesClient(configuration);

        // query for all available process definitions
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        List<ProcessDefinition> processes = queryClient.findProcesses(0, 10);
        System.out.println("\t######### Available processes" + processes);
//        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);

        Long processInstanceId = 2L;

        List<NodeInstance> completedNodes = queryClient.findCompletedNodeInstances(processInstanceId, 0, 10);
        System.out.println("\t######### Completed nodes: " + completedNodes);

        UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
        // find available tasks
        List<org.kie.server.api.model.instance.TaskSummary> tasks = taskClient.findTasksAssignedAsPotentialOwner(user, 0, 10);
        System.out.println("\t######### Tasks: " +tasks);

            request.setAttribute("taskList", tasks);
            ServletContext context = this.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/task.jsp");
            dispatcher.forward(request, response);



//        if (cmd.equals("list")) {
//
//            List<TaskSummary> taskList;
//            try {
//                taskList = runtimeDataService.getTasksAssignedAsPotentialOwner(user, null);
//            } catch (Exception e) {
//                throw new ServletException(e);
//            }
//            request.setAttribute("taskList", taskList);
//            ServletContext context = this.getServletContext();
//            RequestDispatcher dispatcher = context.getRequestDispatcher("/task.jsp");
//            dispatcher.forward(request, res);
//
//        } else if (cmd.equals("approve")) {
//
//            String message;
//            long taskId = Long.parseLong(request.getParameter("taskId"));
//            try {
//                CompositeCommand compositeCommand = new CompositeCommand(new CompleteTaskCommand(taskId, user, null),
//                        new StartTaskCommand(taskId, user));
//                userTaskService.execute(StartupBean.DEPLOYMENT_ID, compositeCommand);
//                message = "Task (id = " + taskId + ") has been completed by " + user;
//                System.out.println(message);
//            } catch (Exception e) {
//                message = "Task operation failed. Please retry : " + e.getMessage();
//                throw new ServletException(e);
//            }
//            request.setAttribute("message", message);
//            ServletContext context = this.getServletContext();
//            RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");
//            dispatcher.forward(request, response);
//
//        }
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
        System.out.println("doPost() from TaskServlet");
        processRequest(request, response);
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