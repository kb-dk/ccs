package dk.kb.ccs.workflow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoaderListener;

/**
 * The workflow scheduler for scheduling the workflows.
 * 
 * Wraps a ScheduledExecutorService, which checks whether to run any of workflows once every second.
 * It is the workflows themselves, who checks their conditions and performs their tasks if the conditions are met.
 */
@Service
public class WorkflowScheduler extends ContextLoaderListener {
    /** The interval for the timer, so it .*/
    protected static final long TIMER_INTERVAL = 1000L;
    
    /** The workflows running in this scheduler.*/
    @Autowired
    CCSWorkflow ccsWorkflow;

    /** The workflows running in this scheduler.*/
    @Autowired
    MailWorkflow mailWorkflow;
    
    /** The timer for running the TimerTasks.*/
    ScheduledExecutorService executorService;
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
        ccsWorkflow.cancel();
        mailWorkflow.cancel();
        executorService.shutdownNow();
    }
    
    /**
     * Scedules the workflows.
     */
    @PostConstruct
    public void scheduleWorkflows() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        
        executorService.scheduleAtFixedRate(ccsWorkflow, TIMER_INTERVAL, TIMER_INTERVAL, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(mailWorkflow, TIMER_INTERVAL, TIMER_INTERVAL, TimeUnit.MILLISECONDS);
    }
}
