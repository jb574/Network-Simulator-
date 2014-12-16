package netsim.tests;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * class to run the all the tests for the app
 * @author Jack Davey
 * @version 7th January 2013
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({stopTests.class,DeviceTests.class,CollisionTests.class
    , framingTests.class,ThirdComputerTests.class
, FileTypeTests.class, QueueingTests.class, goBackNTests.class
})
public class MainTestSuite
{

}
