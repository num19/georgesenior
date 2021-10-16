package georgeSenior;

public class GeorgeSenior
{
  public static void main(String[] args){
    String speech = "jak se jmenuji";
    String response = processing(speech);
    System.out.println(response);
  }

  public static String processing(String speech){
    speech = speech.toLowerCase();
    String response = "";

//Větev s osobními údaji
    if(speech.contains("jak") || speech.contains("kd") ){
           if(speech.contains("jmenuju") ||
        	  speech.contains("jmenuji") ||
             (speech.contains("jméno") && speech.contains("moje")))
        response = getName();

      else if(speech.contains("číslo") && (speech.contains("mobil") ||
              speech.contains("telef")))
        response = getTel();

      else if(speech.contains("email"))
        response = getEmail();

      else if(speech.contains("bydl") ||
              speech.contains("moj") ||
              speech.contains("adres"))
        response = getAddress();

      else if(speech.contains("narodila") ||
             (speech.contains("datum") && speech.contains("narození")))
        response = getBirthDay();

      else if(speech.contains("občan") ||
              speech.contains("průkaz")){
        if(speech.contains("platnost") ||
           speech.contains("končí") ||
           speech.contains("vyprchá"))
          response = getIDValidTo();
        else if(speech.contains("číslo"))
          response = getIDNumber();
      }
    }

//Větev o provedení platby
    if(speech.contains("peníze") ||
      speech.contains("pošli") ||
      speech.contains("poslat") ||
      speech.contains("převést") ||
      speech.contains("plat")){
      if(speech.contains("účet")){
        if(speech.contains("korun"))
          response = transferTo(Integer.parseInt(subsBankAcc(speech)));
        // // Dialogové okno -> kolik?
        // System.out.println("");
      }
      // else if(speech.contains("korun"))
      //   // Dialog. -> komu?
      //   System.out.println("");
      // else
      //   // Dialog -> Komu, kolik?
      //   System.out.println("");
    }

//větev s informacemi o účtu
    if((speech.contains("účtu") || speech.contains("účet")) &&
      ((speech.contains("kolik") && speech.contains("mám")) ||
        speech.contains("jak") ||
        speech.contains("stav")))
      response = getAccBalance();

    if(speech.contains("útrat") ||
      (speech.contains("nejv") && (speech.contains("položk") || speech.contains("výdaj"))) ||
       (speech.contains("kde") || speech.contains("na")) && speech.contains("ušetřit")){
        int index = 0;
      if(speech.contains("druhý") || speech.contains("třetí"))
        index = 1;
      response = getMainExpense(index);
    }

    return(response);
  }

  public static String getMainExpense(int index){
    String[] expenses = {"MC Donald - 95% ", "Rolls-Royce - 5%"};
    return("Největší položkou je: " + expenses[index]);
  }

  public static String getAccBalance(){
    return("999999999 Kč");
  }

  public static String transferTo(int numAcc){
    String bankAcc = Integer.toString(numAcc);
    return("Peníze úspěšně převedeny na účet " + bankAcc);
  }

  public static String subsBankAcc(String speech){
    String[] info = speech.split("učet",-2);
    return(info[info.length-1]);
  }

  public static String getIDNumber(){
    return("999888777");
  }

  public static String getIDValidTo(){
    return("1.1.1991");
  }

  public static String getBirthDay(){
    return("30.2.1900");
  }

  public static String getName(){
    return("Babička Vyližprdka");
  }

  public static String getTel(){
    return("+420123456789");
  }

  public static String getEmail(){
    return("hemo@roidy.mnam");
  }

  public static String getAddress(){
    return("Falešná 123, Springfield");
  }

}
