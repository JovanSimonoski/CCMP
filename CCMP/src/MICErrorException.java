public class MICErrorException extends Exception {
    public MICErrorException(String micCalculated, String micReceived) {
        super("MIC didn't pass! \n" + "MIC Calculated: " + micCalculated + "\nMIC Received: " + micReceived);
    }
}
