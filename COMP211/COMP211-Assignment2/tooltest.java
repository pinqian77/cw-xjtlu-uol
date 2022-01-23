public class tooltest {
    public static void main(String[] args){
        String content = "A0z";
        byte[] content_char = content.getBytes();
        for (byte ch : content_char) {
            System.out.println("ch: " + ch + "(int)ch: " + (int)ch + "byte");
        }

    };
    
}
