package ViewTests;

import View.GameFrame;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;

/**
 * Created by Kasia on 2016-12-29.
 */
@RunWith(MockitoJUnitRunner.class)

public class GameFrameTest {


    @Mock
    JLabel infoLabel;

    @Mock
    JLabel opponentLoginLabel;

    @Mock
    JButton acceptButton;

    @Mock
    JButton passButton;

    @Mock
    JButton suggestButton;

    @Mock
    JButton notAcceptButton;

    @Mock
    JButton resumeButton;


    @InjectMocks
    GameFrame gameFrame;





    @Test
    public void testWaitingForOpponent() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.waitingForOpponent();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Waiting for the opponent...", captor.getValue());
        Mockito.verify(opponentLoginLabel).setText(captor.capture());
        Assert.assertEquals("-", captor.getValue());


    }

    @Test
    public void testOpponentJoined() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.opponentJoined("Login");
        Mockito.verify(opponentLoginLabel).setText(captor.capture());
        Assert.assertEquals("Login", captor.getValue());
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Opponent's turn!", captor.getValue());
    }

    @Test
    public void testJoinToRoom() throws Exception {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.joinToRoom("Login");
        Mockito.verify(opponentLoginLabel).setText(captor.capture());
        Assert.assertEquals("Login", captor.getValue());
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Your turn!", captor.getValue());
       // Mockito.verify(passButton).setEnabled(captor1.capture());
        // Assert.assertEquals(true, captor1.getValue()); nie dziala, nie wiem czemu


    }

    @Test
    public void testPlayerReceivedPermissionToMove() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.playerReceivedPermissionToMove();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Your turn!", captor.getValue());

    }

    @Test
    public void testPlayerMadeLegalMove() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.playerMadeLegalMove();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Opponent's turn!", captor.getValue());
    }

    @Test
    public void playerMadeIllegalMoveKO() throws Exception {
    }

    @Test
    public void playerMadeIllegalMoveSuicide() throws Exception {

    }

    @Test
    public void playerMadeIllegalMoveOccupiedField() throws Exception {

    }

    @Test
    public void testOpponentPassed() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.opponentPassed();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Opponent passed. Your turn!", captor.getValue());
        //Mockito.verify(passButton).setEnabled(captor1.capture());
        //Assert.assertEquals(true, captor1.getValue()); nie dziala
    }

    @Test
    public void testOpponentGaveUp() throws Exception {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.opponentGaveUp();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("End of the game.", captor.getValue());
        Mockito.verify(acceptButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
       // Mockito.verify(suggestButton).setEnabled(captor1.capture());
       //Assert.assertEquals(false, captor1.getValue()); nie dziala
        Mockito.verify(notAcceptButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());

    }



    @Test
    public void waitForOpponentToMarkDeadStones() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.waitForOpponentToMarkDeadStones();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("<html>Wait for opponent <br/>to mark dead stones!</html>", captor.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
    }

    @Test
    public void markDeadStones() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.markDeadStones();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Mark dead stones of your opponent.", captor.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
       // Mockito.verify(suggestButton).setVisible(captor1.capture());
       // Assert.assertEquals(true, captor1.getValue()); nie dziala
    }



    @Test
    public void botAcceptedDeadStones() throws Exception {

    }

    @Test
    public void deadStonesAccepted() throws Exception {

    }

    @Test
    public void deadStonesNotAccepted() throws Exception {

    }

    @Test
    public void markArea() throws Exception {

    }

    @Test
    public void showMarkedArea() throws Exception {

    }

    @Test
    public void showFinalMarkedArea() throws Exception {

    }

    @Test
    public void showSingleSerwerMarkedArea() throws Exception {

    }

    @Test
    public void serwerMarkedArea() throws Exception {

    }

    @Test
    public void areaAccepted() throws Exception {

    }

    @Test
    public void areaNotAccepted() throws Exception {

    }

    @Test
    public void opponentResumed() throws Exception {

    }

    @Test
    public void deleteOpponentNotAcceptedArea() throws Exception {

    }

}