/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.foo.front.web;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kie.api.task.TaskService;
import org.kie.internal.SystemEventListenerFactory;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;

/**
 *
 * @author Shaun
 */
public class UnitTest {
    private KieServicesClient kieServicesClient;

    public UnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        String serverUrl = "http://localhost:8080/kie-server/services/rest/server";
        String userServer = "kieserver";
        String passwordServer = "kieserver1!";
        KieServicesConfiguration configuration = KieServicesFactory.newRestConfiguration(serverUrl, userServer, passwordServer);
        configuration.setMarshallingFormat(MarshallingFormat.JAXB);
//        configuration.addJaxbClasses(extraClasses);
//        KieServicesClient kieServicesClient =  KieServicesFactory.newKieServicesClient(configuration, kieContainer.getClassLoader());
        kieServicesClient =  KieServicesFactory.newKieServicesClient(configuration);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void hello()
    {
        String user = "jiri";

        // query for all available process definitions
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        List<ProcessDefinition> processes = queryClient.findProcesses(0, 10);
        System.out.println("\t######### Available processes" + processes);
        System.out.println("\t######### process count: " + processes.size());
        //        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        
//        test1(processes, queryClient);

        Long processInstanceId = 7L;

        List<NodeInstance> completedNodes = queryClient.findCompletedNodeInstances(processInstanceId, 0, 10);
        System.out.println("\t######### Completed nodes: " + completedNodes);
        System.out.println("\t######### completed node count: " + completedNodes.size());

        List<NodeInstance> activeNodes = queryClient.findActiveNodeInstances(processInstanceId, 0, 10);
        System.out.println("\t######### Active nodes: " + activeNodes);
        System.out.println("\t######### active node count: " + activeNodes.size());
        
        List<NodeInstance> nodes = queryClient.findNodeInstances(processInstanceId, 0, 10);
        System.out.println("\t######### Nodes: " +nodes);
        System.out.println("\t######### node count: " +nodes.size());
        for (NodeInstance node : nodes)
        {
            System.out.println("\t.... Nodes: " +node.getName());
            System.out.println("\t.... completed: " +node.getCompleted());
            if (!node.getCompleted())
            {
                System.out.println("\t.... completing node: ");
                node.setCompleted(Boolean.TRUE);
            }
        }

        UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
        // find available tasks
        List<org.kie.server.api.model.instance.TaskSummary> tasks = taskClient.findTasksAssignedAsPotentialOwner(user, 0, 10);
        System.out.println("\t######### Tasks: " +tasks);
        System.out.println("\t######### task count: " +tasks.size());
        
        taskClient.findTasks(user, Integer.BYTES, Integer.BYTES);
    }

    private void test2()
    {
        String containerId = "foo";
        KieContainerResourceList containers = kieServicesClient.listContainers().getResult();
        if (containers != null) {
            for (KieContainerResource kieContainerResource : containers.getContainers()) {
                if (kieContainerResource.getContainerId().equals(containerId)) {
//                    kieContainerResource.
                    break;
                }
            }
        }
    }

    private void test1(List<ProcessDefinition> processes, QueryServicesClient queryClient)
    {
        for (ProcessDefinition pd : processes)
        {
            String processInstanceId = pd.getId();

            System.out.println("\t.... process id: " +processInstanceId);
            System.out.println("\t.... process name: " +pd.getName());

//            List<NodeInstance> completedNodes = queryClient.findCompletedNodeInstances(processInstanceId, 0, 10);
//            System.out.println("\t.... Completed nodes: " + completedNodes);
//            System.out.println("\t.... completed node count: " + completedNodes.size());
//
//            List<NodeInstance> activeNodes = queryClient.findActiveNodeInstances(processInstanceId, 0, 10);
//            System.out.println("\t.... Active nodes: " + activeNodes);
//            System.out.println("\t.... active node count: " + activeNodes.size());
//
//            List<NodeInstance> nodes = queryClient.findNodeInstances(processInstanceId, Integer.BYTES, Integer.BYTES);
//            System.out.println("\t.... Nodes: " +nodes);
//            System.out.println("\t.... node count: " +nodes.size());
        }
                
    }
}
