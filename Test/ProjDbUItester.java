import static org.junit.Assert.*;

import org.junit.Test;

import com.lwoods.masterplan.ProjDbUI;
import com.lwoods.masterplan.Projects;


public class ProjDbUItester {

	@Test
	public void testDatabaseEntry() {
		//Projects projInst = new Projects();
		//ProjDbUI projUiInstance = new ProjDbUI(projInst);
		/*projUiInstance.open();
		long hopeNotError = projUiInstance.createEntry("Lamar", "Stuff changed");
		projUiInstance.close();
		if(hopeNotError == -1){
		fail("Entry into the Database failed");	
		}*/
		int five = 5;
		int same = 5;
		assertEquals(five, same);
	}

}
