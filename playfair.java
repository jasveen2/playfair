public class playfair{

  public static void main(String[] args) {
    if (args[0].equals("encode"))
      encode(args[1], args[2]);
    else
      decode(args[1], args[2]);
  }

  public static char[][] breakup(String key){
    char[][] keytext;
    keytext = new char[5][5];
    int row = 0;
    int column = 0;
    for (char i : key.toCharArray()){
      if (column == 5){
        row++;
        column = 0;
      }
      keytext[row][column] = i;
      column++;
    }
    return keytext;
  }

  public static String encode(String ciphertext, String keytext){
    char[][] keys = breakup(keytext);
    ciphertext = ciphertext.replaceAll("\\s+", "");
    ciphertext = ciphertext.replaceAll("J", "I");
    ciphertext = split2(ciphertext);
    while (needsshift(ciphertext)){
      for (int i = 0; i < ciphertext.length(); i++) {
        if (i != ciphertext.length() - 1) { //not the last letter
          if ((ciphertext.charAt(i)) == ciphertext.charAt(i + 1)) {
            ciphertext = insert(ciphertext, "X", i + 1);
            ciphertext = ciphertext.replaceAll("\\s+", "");
            ciphertext = split2(ciphertext);
          }
        }
      }
    }
    ciphertext = ciphertext.trim();
    if (ciphertext.charAt(ciphertext.length() - 2) == ' '){
      ciphertext = ciphertext.concat("X");
    }
    String[] ct = ciphertext.split("\\s+");
    String encoded = "";
    for (String c : ct){
      int rowa = 0;
      int cola = 0;
      int rowb = 0;
      int colb = 0;
      for (int i = 0; i < 5; i++)
        for (int j = 0; j < 5; j++)
          if (c.charAt(0) == keys[i][j]){
            rowa = i;
            cola = j;
          }
      for (int i = 0; i < 5; i++)
        for (int j = 0; j < 5; j++)
          if (c.charAt(1) == keys[i][j]){
            rowb = i;
            colb = j;
          }
      if (rowa == rowb)
        c = verticalEncode(true, keys, rowa, rowb, cola, colb);
      else
        if (cola == colb)
        c = horizontalEncode(true, keys, rowa, rowb, cola, colb);
      else
        c = regularEncode(keys, rowa, rowb, cola, colb);
      encoded += c;
    }
    System.out.println(encoded);
    return(encoded);
  }

  public static String decode(String plaintext, String keytext){
    char[][] keys = breakup(keytext);
    plaintext = plaintext.replaceAll("J", "I");
    plaintext = split2(plaintext);
    String[] pt = plaintext.split("\\s+");
    String decoded = "";
    for (String c : pt){
      int rowa = 0;
      int cola = 0;
      int rowb = 0;
      int colb = 0;
      for (int i = 0; i < 5; i++)
        for (int j = 0; j < 5; j++)
          if (c.charAt(0) == keys[i][j]){
            rowa = i;
            cola = j;
          }
      for (int i = 0; i < 5; i++)
        for (int j = 0; j < 5; j++)
          if (c.charAt(1) == keys[i][j]){
            rowb = i;
            colb = j;
          }
      if (rowa == rowb)
        c = verticalEncode(false, keys, rowa, rowb, cola, colb);
      else
      if (cola == colb)
        c = horizontalEncode(false, keys, rowa, rowb, cola, colb);
      else
        c = regularEncode(keys, rowa, rowb, cola, colb);
      decoded += c;
    }
    System.out.println(decoded);
    return decoded;
  }

  public static String insert(String text, String insert, int index){
    String begin = text.substring(0, index);
    String end = text.substring(index);
    return (begin + insert + end);
  }

  public static Boolean needsshift(String text){
    String t[] = text.split("\\s+");
    for (String i : t) {
      if (i.length() == 2)
        if ((i.charAt(0)) == i.charAt(1))
          return true;
    }
    return false;
  }

  public static String split2(String text){
    int count = 0;
    for (int j = 0; j < text.length(); j++){
      if (count == 2) {
        text = insert(text, " ", j);
        count = 0;
      }
      else
        count++;
    }
    return text;
  }

  public static String verticalEncode(boolean ltr, char keytext[][], int rowa, int rowb, int cola, int colb) {
    int add = 0;
    if (ltr) {
      if (rowa == 4) {
        rowa = 0;
        rowb = 0;
      }
      add = 1;
    } else {
      if (rowa == 0) {
        rowa = 4;
        rowb = 4;
      }
      add = -1;
    }
    rowa += add;
    rowb += add;
    String a = "";
    a += keytext[rowa][cola] + "";
    a += keytext[rowb][colb] + "";
    return a;
  }

  public static String horizontalEncode(boolean ltr, char keytext[][], int rowa, int rowb, int cola, int colb){
    int add = 0;
    if (ltr) {
      if (cola == 4) {
        cola = 0;
        colb = 0;
      }
      add = 1;
    }
    else {
      if (cola == 0) {
        cola = 4;
        colb = 4;
      }
      add = -1;
    }
      cola += add;
      colb += add;
    String a = "";
    a += keytext[rowa][cola] + "";
    a += keytext[rowb][colb] + "";
    return a;
  }

  public static String regularEncode(char keytext[][], int rowa, int rowb, int cola, int colb){
    String a = "";
    a += keytext[rowa][colb] + "";
    a += keytext[rowb][cola] + "";
    return a;
  }

}
