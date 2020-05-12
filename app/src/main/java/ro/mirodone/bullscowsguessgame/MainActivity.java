package ro.mirodone.bullscowsguessgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button button_answer, button_reset, button_quit;

    EditText text_bulls, text_cows;

    TextView tv_output;

    List<Integer> allNumbers; // list of all possible numbers

    int guessNumber = 1234; //number CPU is guessing

    int remainingCount = 0;// remaining possible numbers

    int oldCount = 0;//temp value for the remaining possible numbers

    String outputString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_answer = findViewById(R.id.buttonAnswer);
        button_reset = findViewById(R.id.btn_reset);
        button_quit = findViewById(R.id.btn_quit);
        text_bulls = findViewById(R.id.text_bulls);
        text_cows = findViewById(R.id.text_cows);
        tv_output = findViewById(R.id.tv_output);

        start();

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                allNumbers.clear();
                guessNumber = 1234;
                outputString = "";
                text_cows.clearFocus();
                text_bulls.clearFocus();
                start();

            }
        });

        button_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });



        button_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get input number for bulls
                String bullsString = text_bulls.getText().toString();
                //get input number for cows
                String cowsString = text_cows.getText().toString();

                //check if empty
                if (!bullsString.equals("") && !cowsString.equals("")) {
                    //bulls counting by comparing each of the numbers in the list and see if the number are the same
                    //if remove the number from the list to reduce the size
                    int bullsInt = Integer.parseInt(bullsString);


                    //check bulls if 4
                    if (bullsInt != 4) {

                        List<Integer> tempBulls = new ArrayList<>();

                        for (int i : allNumbers) {
                            if (checkBulls(i) == bullsInt) {
                                tempBulls.add(i);
                            }
                        }

                        //remove all wrong possibilities and keep only the right ones based on the number of bulls
                        allNumbers.clear();
                        allNumbers.addAll(tempBulls);

//cows
                        int cowsInt = Integer.parseInt(cowsString);
                        cowsInt = cowsInt + bullsInt;


                        List<Integer> tempCows = new ArrayList<>();

                        for (int i : allNumbers) {
                            if (checkCows(i) == cowsInt) {
                                tempCows.add(i);
                            }
                        }

                        if (tempCows.size() > 0) {
                            //remove all wrong possibilities and keep only the right ones based on the number of cows
                            allNumbers.clear();
                            allNumbers.addAll(tempCows);
                        }


                        //remaining possibilities
                        remainingCount = allNumbers.size();

                        //check possibilities left  and display

                        if (remainingCount > oldCount || remainingCount == 0) {
                            //mistake
                            outputString = outputString + "You are lying or you made a mistake!\n";
                            tv_output.setText(outputString);
                        } else if (remainingCount == 1) {
                            guessNumber = allNumbers.get(0);
                            outputString = outputString + "Your number is " + guessNumber + "\n";
                            tv_output.setText(outputString);
                        } else {
                            oldCount = remainingCount;
                            outputString = outputString + "Remaining possibilities " + remainingCount + "\n";
                            //guess the next available number
                            guessNumber = allNumbers.get(0);
                            outputString = outputString + "My guess is " + guessNumber + "\n";
                            tv_output.setText(outputString);
                        }


                    } else {
                        outputString = outputString + "Great! Your number is " + guessNumber + "\n";
                        tv_output.setText(outputString);
                        showAlertDialog();
                    }
                } else {
                    outputString = outputString + "Wrong input!\n";
                    tv_output.setText(outputString);
                    showAlertDialog();
                }
            }
        });

    }


    public void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to try again?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finishAndRemoveTask();
            }
        });
        alertDialog.create().show();
    }


    public void start() {

        allNumbers = new ArrayList<>();

        //add all numbers to the main list
        //no zeros, no duplicates
        for (int i = 1234; i <= 9876; i++) {
            if (!checkForZero(i) && !checkForDuplicates(i)) {
                allNumbers.add(i);
            }
        }

        //calculate the possibilities and make the first guess
        remainingCount = allNumbers.size();
        oldCount = remainingCount;
        outputString = outputString + "Remaining possibilities " + remainingCount + "\n";
        outputString = outputString + "My guess is " + guessNumber + "\n";
        tv_output.setText(outputString);

    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
        //  super.onBackPressed();
    }

    // check for bulls in a number
    private int checkBulls(int all) {
        int output = 0;

        if (getFirstDigit(guessNumber) == getFirstDigit(all)) {
            output = output + 1;
        }

        if (getSecondDigit(guessNumber) == getSecondDigit(all)) {
            output = output + 1;
        }

        if (getThirdDigit(guessNumber) == getThirdDigit(all)) {
            output = output + 1;
        }

        if (getForthDigit(guessNumber) == getForthDigit(all)) {
            output = output + 1;
        }

        return output;
    }

    private int checkCows(int all) {
        int output = 0;

        //add all digits from the guesses number to an array
        List<Integer> guessedArray = new ArrayList<>();
        guessedArray.add(getFirstDigit(guessNumber));
        guessedArray.add(getSecondDigit(guessNumber));
        guessedArray.add(getThirdDigit(guessNumber));
        guessedArray.add(getForthDigit(guessNumber));

        //add all digits from the list of numbers to an array

        List<Integer> allNumbersArray = new ArrayList<>();
        allNumbersArray.add(getFirstDigit(all));
        allNumbersArray.add(getSecondDigit(all));
        allNumbersArray.add(getThirdDigit(all));
        allNumbersArray.add(getForthDigit(all));

        //add a cow for each of the same digits in the

        for (int x = 0; x < guessedArray.size(); x++) {
            for (int y = 0; y < allNumbersArray.size(); y++) {
                if (guessedArray.get(x) == allNumbersArray.get(y)) {
                    output = output + 1;
                }
            }
        }

        return output;
    }


    private boolean checkForZero(int number) {
        if (getFirstDigit(number) == 0 || getSecondDigit(number) == 0 || getThirdDigit(number) == 0 || getForthDigit(number) == 0) {
            return true;
        } else {
            return false;
        }
    }

    //check if number have duplicates

    private boolean checkForDuplicates(int duplicate) {
        boolean output = false;

        if (getFirstDigit(duplicate) == getSecondDigit(duplicate)) {
            output = true;
        }
        if (getFirstDigit(duplicate) == getThirdDigit(duplicate)) {
            output = true;
        }
        if (getFirstDigit(duplicate) == getForthDigit(duplicate)) {
            output = true;
        }
        if (getSecondDigit(duplicate) == getThirdDigit(duplicate)) {
            output = true;
        }
        if (getSecondDigit(duplicate) == getForthDigit(duplicate)) {
            output = true;
        }
        if (getThirdDigit(duplicate) == getForthDigit(duplicate)) {
            output = true;
        }
        return output;
    }

    // get all digits separate

    private int getFirstDigit(int num) {
        return num / 1000;
    }

    private int getSecondDigit(int num) {
        return (num - getFirstDigit(num) * 1000) / 100;
    }

    private int getThirdDigit(int num) {
        return (num - getFirstDigit(num) * 1000 - getSecondDigit(num) * 100) / 10;
    }

    private int getForthDigit(int num) {
        return num - getFirstDigit(num) * 1000 - getSecondDigit(num) * 100 - getThirdDigit(num) * 10;
    }

}
