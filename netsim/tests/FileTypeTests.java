package netsim.tests;
import netsim.Simulation.SignalTypes;
import org.junit.Test;

/**
 * class to give the file selection
 * functionality a good workout
 * @author Jack Davey
 * @version 7th february 2014
 */
public class FileTypeTests
{


    /**
     * method to ensure all the correct options are there
     *
     */
     @Test
     public  void ensureRightOptionsPresent()
     {
         SignalTypes tester = SignalTypes.obtain();
         boolean result = true;
         result =  optionExists(tester,"page");
         result = optionExists(tester,"tune");
         result = optionExists(tester,"film");
         assert(result == true);
     }





    /**
     * private methdo to check wher an item is contained
     * in the settings list
     * @param testSubject  the object we use to do the testing
     * @param choice  option we want to make sure is there
     * @return a boolean indicating the outome of the test
     */
    private boolean optionExists(SignalTypes testSubject,String choice)
    {
        try
        {
           testSubject.setCurrentMedium(choice);
        }
        catch (IllegalArgumentException notFound)
        {
            return false;
        }
        return true;
    }


}
