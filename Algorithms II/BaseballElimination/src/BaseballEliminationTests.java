import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class BaseballEliminationTests {
    @Test
    public void BaseballElimination_IsEliminated_IsTrue() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        boolean isEliminated = be.isEliminated("Philadelphia");
        assertTrue(isEliminated);

        // trivial elim
        isEliminated = be.isEliminated("Montreal");
        assertTrue(isEliminated);
    }

    @Test
    public void BaseballElimination_IsEliminated_IsTrue_China() {
        BaseballElimination be = new BaseballElimination("data\\teams7.txt");
        
        // trivial elim
        boolean isEliminated = be.isEliminated("China");
        assertTrue(isEliminated);
    }
    
    @Test
    public void BaseballElimination_IsEliminated_IsFalse() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        boolean isEliminated = be.isEliminated("New_York");
        assertFalse(isEliminated);

        isEliminated = be.isEliminated("Atlanta");
        assertFalse(isEliminated);
    }

    // java.lang.IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_IsEliminated_Throws() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.isEliminated("foobar");
    }

    @Test
    public void BaseballElimination_ReturnsNumberOfTeams_4() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        int teams = be.numberOfTeams();
        assertEquals(4, teams);
    }

    @Test
    public void BaseballElimination_ReturnsNumberOfTeams_1() {
        BaseballElimination be = new BaseballElimination("data\\teams1.txt");
        int teams = be.numberOfTeams();
        assertEquals(1, teams);
    }

    @Test
    public void BaseballElimination_Teams_Returns4() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        Iterable<String> teams = be.teams();

        List<String> copy = CopyAndSortTeams(teams);
        assertEquals(4, copy.size());

        assertEquals("Atlanta", copy.get(0));
        assertEquals("Montreal", copy.get(1));
        assertEquals("New_York", copy.get(2));
        assertEquals("Philadelphia", copy.get(3));
    }

    @Test
    public void BaseballElimination_Wins_Returns() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        int result = be.wins("Atlanta");
        assertEquals(83, result);
    }

    // java.lang.IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_Wins_Throws() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.wins("foobar");
    }

    @Test
    public void BaseballElimination_Losses_Returns() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        int result = be.losses("Atlanta");
        assertEquals(71, result);
    }

    // java.lang.IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_Losses_Throws() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.losses("foobar");
    }

    @Test
    public void BaseballElimination_Remaining_Returns() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        int result = be.remaining("Atlanta");
        assertEquals(8, result);
    }

    // java.lang.IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_Remaining_Throws() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.remaining("foobar");
    }

    @Test
    public void BaseballElimination_Against_Returns() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        int result = be.against("Atlanta", "New_York");
        assertEquals(6, result);
    }

    @Test
    public void BaseballElimination_AgainstSelf_ReturnsZero() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        int result = be.against("Atlanta", "Atlanta");
        assertEquals(0, result);
    }

    // java.lang.IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_Against_Throws() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.against("foobar", "New_York");
    }

    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_Against_Throws2() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.against("New_York", "foobar");
    }
    
    @Test
    public void BaseballElimination_Certificate_Iliminated1() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        Iterable<String> isEliminated = be.certificateOfElimination("Philadelphia");
        List<String> teams = this.CopyAndSortTeams(isEliminated);
        assertEquals(2, teams.size());
        assertTrue(teams.contains("New_York"));
        assertTrue(teams.contains("Atlanta"));
    }
    
    @Test
    public void BaseballElimination_Certificate_Iliminated_Trivial() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        Iterable<String> isEliminated = be.certificateOfElimination("Montreal");
        List<String> teams = this.CopyAndSortTeams(isEliminated);
        assertEquals(1, teams.size());
        assertTrue(teams.contains("Atlanta"));
    }

    @Test
    public void BaseballElimination_Certificate_NotIliminated() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        Iterable<String> isEliminated = be.certificateOfElimination("Atlanta");
        assertNull(isEliminated);
        
        isEliminated = be.certificateOfElimination("New_York");
        assertNull(isEliminated);
    }

    // java.lang.IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void BaseballElimination_Certificate_Throws() {
        BaseballElimination be = new BaseballElimination("data\\teams4.txt");
        be.certificateOfElimination("foobar");
    }
    
    private List<String> CopyAndSortTeams(Iterable<String> teams) {
        Iterator<String> iter = teams.iterator();
        List<String> copy = new ArrayList<String>();
        while (iter.hasNext())
            copy.add(iter.next());

        Collections.sort(copy);
        return copy;
    }
}
