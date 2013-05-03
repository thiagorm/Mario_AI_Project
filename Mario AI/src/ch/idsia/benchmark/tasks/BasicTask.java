package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy,
 * sergey@idsia.ch
 * Date: Mar 14, 2010 Time: 4:47:33 PM
 */

public class BasicTask implements Task
{
protected final static Environment environment = MarioEnvironment.getInstance();
private Agent agent;
protected CmdLineOptions options;
private long COMPUTATION_TIME_BOUND = 42; // stands for prescribed  FPS 24.
private String name = getClass().getSimpleName();

//CUIDADO
int maxTime = Integer.MAX_VALUE;
private String enviromentAtributes =  "-lco on -lb on -le off -lhb on -lg on -ltb on -lhs on -lc on -lde on -lf on -ll 40 -tl " +  maxTime + " -fps 24 -gv off"; 

public BasicTask(CmdLineOptions cmdLineOptions)
{
    this.setOptions(cmdLineOptions);
}

public BasicTask()
{

}

/**
 * @return boolean flag whether controller is disqualified or not
 */
public boolean runOneEpisode()
{
    while (!environment.isLevelFinished())
    {
        environment.tick();
        if (!GlobalOptions.isGameplayStopped)
        {
            agent.integrateObservation(environment);
            agent.giveIntermediateReward(environment.getIntermediateReward());

            boolean[] action = agent.getAction();
            environment.performAction(action);
        }
        if(environment.isLevelFinished())
        	environment.reset(enviromentAtributes);
    }
    environment.closeRecorder();
    environment.getEvaluationInfo().setTaskName(name);
    return true;
}

public void reset(CmdLineOptions cmdLineOptions)	
{
    options = cmdLineOptions;
    agent = options.getAgent();
    environment.reset(cmdLineOptions);
    agent.reset();
}

public void reset(Agent agent)	
{
    //options = cmdLineOptions;
    this.agent = agent;
    environment.reset(enviromentAtributes);
    agent.reset();
}

public Environment getEnvironment()
{
    return environment;
}

public float[] evaluate(Agent controller)
{
    return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
}

public void setOptions(CmdLineOptions options)
{
    this.options = options;
}

public CmdLineOptions getOptions()
{
    return options;
}

public void doEpisodes(int amount, boolean verbose)
{
    for (int i = 0; i < amount; ++i)
    {
        this.reset(options);
        this.runOneEpisode();
        if (verbose)
            System.out.println(environment.getEvaluationInfoAsString());
    }
}

public boolean isFinished()
{
    return false;
}

public void reset()
{

}

public String getName()
{
    return name;
}
}

//            start timer
//            long tm = System.currentTimeMillis();

//            System.out.println("System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND = " + (System.currentTimeMillis() - tm ));
//            if (System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND)
//            {
////                # controller disqualified on this level
//                System.out.println("Agent is disqualified on this level");
//                return false;
//            }
