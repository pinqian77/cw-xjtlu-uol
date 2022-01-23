/*************************************
 * Filename:  Sender.java
 *************************************/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Sender extends NetworkHost

{
    /*
     * Predefined Constant (static member variables):
     *
     *   int MAXDATASIZE : the maximum size of the Message data and
     *                     Packet payload
     *
     *
     * Predefined Member Methods:
     *
     *  void startTimer(double increment):
     *       Starts a timer, which will expire in
     *       "increment" time units, causing the interrupt handler to be
     *       called.  You should only call this in the Sender class.
     *  void stopTimer():
     *       Stops the timer. You should only call this in the Sender class.
     *  void udtSend(Packet p)
     *       Puts the packet "p" into the network to arrive at other host
     *  void deliverData(String dataSent)
     *       Passes "dataSent" up to app layer. You should only call this in the 
     *       Receiver class.
     *  double getTime()
     *       Returns the current time in the simulator.  Might be useful for
     *       debugging.
     *  void printEventList()
     *       Prints the current event list to stdout.  Might be useful for
     *       debugging, but probably not.
     *
     *
     *  Predefined Classes:
     *
     *  Message: Used to encapsulate a message coming from app layer
     *    Constructor:
     *      Message(String inputData): 
     *          creates a new Message containing "inputData"
     *    Methods:
     *      boolean setData(String inputData):
     *          sets an existing Message's data to "inputData"
     *          returns true on success, false otherwise
     *      String getData():
     *          returns the data contained in the message
     *  Packet: Used to encapsulate a packet
     *    Constructors:
     *      Packet (Packet p):
     *          creates a new Packet, which is a copy of "p"
     *      Packet (int seq, int ack, int check, String newPayload)
     *          creates a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and a
     *          payload of "newPayload"
     *      Packet (int seq, int ack, int check)
     *          chreate a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and
     *          an empty payload
     *    Methods:
     *      boolean setSeqnum(int n)
     *          sets the Packet's sequence field to "n"
     *          returns true on success, false otherwise
     *      boolean setAcknum(int n)
     *          sets the Packet's ack field to "n"
     *          returns true on success, false otherwise
     *      boolean setChecksum(int n)
     *          sets the Packet's checksum to "n"
     *          returns true on success, false otherwise
     *      boolean setPayload(String newPayload)
     *          sets the Packet's payload to "newPayload"
     *          returns true on success, false otherwise
     *      int getSeqnum()
     *          returns the contents of the Packet's sequence field
     *      int getAcknum()
     *          returns the contents of the Packet's ack field
     *      int getChecksum()
     *          returns the checksum of the Packet
     *      String getPayload()
     *          returns the Packet's payload
     *
     */

    // Add any necessary class variables here. They can hold
    // state information for the sender.
    private final int ACKNUMFINAL = Integer.MIN_VALUE; // set ackNum immutable since it does not need in sender
    private final int WINDOWSIZE = 8;  // set window size immutable(must less than 2^n)
    private final double INC = 102;  // set time out increase immutable

    private int baseIndex = 0;   // initialize base index to 0
    private int theClosestFreeSeqNum = 0; // initialize the closest free sequence number to 0
    private Boolean isRunning = false; // to check if the timer is running
    private Queue<Message> senderMessageBuffer = new LinkedList<>(); // using a linked list queue to store messages waiting to be sent(we can ignore max buffer size)
    private List<Packet> prePackets = new ArrayList<>(); // using a array list to store previous sent packets since then can be get by index faster 

    // Also add any necessary methods (e.g. for checksumming)
    // compute checksum
    private static int computeChecksum(String payload, int seq, int ack){
        String data = payload + Integer.toString(seq) + Integer.toString(ack);
        int res = 0;
        for (char ch : data.toCharArray()) {
            res += (int)ch;
        }
        
        return res;
    }
    
    // This is the constructor. Don't touch!!!
    public Sender(int entityName,
                  EventList events,
                  double pLoss,
                  double pCorrupt,
                  int trace,
                  Random random)
    {
        super(entityName, events, pLoss, pCorrupt, trace, random);
    }

    // This routine will be called whenever the app layer at the sender
    // has a message to send.  The job of your protocol is to insure that
    // the data in such a message is delivered in-order, and correctly, to
    // the receiving application layer.
    protected void Output(Message message){
        //if sliding window still has the free seq number left, then send the message.
        if(baseIndex + WINDOWSIZE > theClosestFreeSeqNum){
            // get payload from message since more than once it will be used
            String payload = message.getData();

            // compute sender side checksum
            int checksum = computeChecksum(payload, theClosestFreeSeqNum, ACKNUMFINAL);

            // create the packet we are going to send
            Packet curPacket = new Packet(theClosestFreeSeqNum, ACKNUMFINAL, checksum, payload);

            // store the packet in case we have to send it again
            prePackets.add(curPacket);

            // send the packet and Sender enters waiting state
            udtSend(curPacket);

            // time counter begins
            // if(!isRunning(baseIndex, theClosestFreeSeqNum)){
            //     startTimer(INC);
            // }
            if(!isRunning){
                startTimer(INC);
                isRunning = true;
            }

            // increase sequence number
            theClosestFreeSeqNum += 1;
        }
        // or store the packet into linked sender buffer
        else{
            senderMessageBuffer.add(message);
        }
    }
    
    // This routine will be called whenever a packet sent from the receiver 
    // (i.e. as a result of a udtSend() being done by a receiver procedure)
    // arrives at the sender.  "packet" is the (possibly corrupted) packet
    // sent from the receiver.
    protected void Input(Packet packet){
        // get data of the packet from receiver
        String payloadFromReceiver = packet.getPayload();
        int seqNumFromReceiver = packet.getSeqnum();
        int ackNumFromReceiver = packet.getAcknum();
        int feedbackChecksumReceiverSide = packet.getChecksum();

        // compute the feedback packet's checksum on sender side
        int feedbackChecksumSenderSide = computeChecksum(payloadFromReceiver, seqNumFromReceiver, ackNumFromReceiver);

        // check if feedback packet is corrupt
        boolean isCorrupt = feedbackChecksumSenderSide == feedbackChecksumReceiverSide ? false : true;

        // stop timer and update sequence number if condition is right
        if(!isCorrupt && baseIndex <= ackNumFromReceiver){
            stopTimer();
            isRunning = false;
            baseIndex = ackNumFromReceiver + 1;
            // if we still have packet to send 
            if(baseIndex != theClosestFreeSeqNum){
                startTimer(INC);
                isRunning = true;
                while(!senderMessageBuffer.isEmpty() && baseIndex + WINDOWSIZE > theClosestFreeSeqNum){
                    Output(senderMessageBuffer.poll());
                }
            }
        }
        // else ACK packet that we are expected is not received.
    }
    
    // This routine will be called when the senders's timer expires (thus 
    // generating a timer interrupt). You'll probably want to use this routine 
    // to control the retransmission of packets. See startTimer() and 
    // stopTimer(), above, for how the timer is started and stopped. 
    protected void TimerInterrupt(){
        // for every interrupt, resend every packets in the window from the base index
        startTimer(INC);
        isRunning = true;
        for(int i = baseIndex; i < theClosestFreeSeqNum; i += 1){
            udtSend(prePackets.get(i));
        }
    }
    
    // This routine will be called once, before any of your other sender-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of the sender).
    protected void Init()
    {
    }

}
