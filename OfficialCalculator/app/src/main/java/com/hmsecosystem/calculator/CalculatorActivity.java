/*
      Copyright 2021. Futurewei Technologies Inc. All rights reserved.
      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at
        http:  www.apache.org/licenses/LICENSE-2.0
      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
*/

package com.hmsecosystem.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private EditText screen;
    private  boolean operator, hasdot;

    private static final String TAG = "CalculatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        screen = findViewById(R.id.screen);
        screen.setFocusable(false);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    public void add0(View v) {
        operator = false;
        if(screen.getText().toString().compareToIgnoreCase("Infinity") == 0 ||
                (screen.getText().length() == 1 && screen.getText().charAt(0) == '0'))
            screen.setText("");
        screen.setText(screen.getText() + "0");
        moveCaret();
    }

    public void add1(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0') ||
                screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "1");
        moveCaret();
    }

    public void add2(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "2");
        moveCaret();
    }

    public void add3(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "3");
        moveCaret();
    }

    public void add4(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "4");
        moveCaret();
    }

    public void add5(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "5");
        moveCaret();
    }

    public void add6(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "6");
        moveCaret();
    }

    public void add7(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "7");
        moveCaret();
    }

    public void add8(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "8");
        moveCaret();
    }

    public void add9(View v) {
        operator = false;
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "9");
        moveCaret();
    }

    public void sum(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }
            screen.setText(screen.getText().toString() + " + ");
            operator = true;
            hasdot = false;
            moveCaret();
        }
    }

    public void sub(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            screen.setText(screen.getText() + " - ");
            operator = true;
            hasdot = false;
            moveCaret();
        }
    }

    public void div(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            screen.setText(screen.getText() + " / ");
            operator = true;
            hasdot = false;
            moveCaret();
        }
    }

    public void mult(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            screen.setText(screen.getText().toString() + " * ");
            operator = true;
            hasdot = false;
            moveCaret();
        }
    }

    public void sqrt(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) &&
                !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            String expression = getLastDigitedNumber();
            String answer = Double.toString(Math.sqrt(Double.parseDouble(expression)));
            hasdot = doesItHasADot(answer);
            if(answer.charAt(answer.length() - 1) == '0' && answer.charAt(answer.length() - 2) == '.') {
                answer = answer.substring(0, answer.length() - 2);
                hasdot = false;
            }
            screen.setText(screen.getText() + answer);
            moveCaret();
        }
    }

    public void powto2(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            String expression = getLastDigitedNumber();
            String answer = Double.toString(Math.pow(Double.parseDouble(expression), 2));
            hasdot = doesItHasADot(answer);
            if(answer.charAt(answer.length() - 1) == '0' && answer.charAt(answer.length() - 2) == '.') {
                answer = answer.substring(0, answer.length() - 2);
                hasdot = false;
            }
            screen.setText(screen.getText() + answer);
            moveCaret();
        }
    }

    public void fat(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            String expression = getLastDigitedNumber();
            long toInt = (long)Double.parseDouble(expression);
            if(toInt <= 20) {
                long answer = 1;
                for (int i = 2; i <= toInt; i++) {
                    answer *= i;
                }
                screen.setText(screen.getText() + Long.toString(answer));
            } else {
                screen.setText(screen.getText() + "0");
                displayResult("Factorial values less than 21 only");
            }
            hasdot = false;
            moveCaret();
        }
    }
    public void plusminus(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            String expression = getLastDigitedNumber();
            String d = Double.toString(Double.parseDouble(expression) * -1);
            hasdot = doesItHasADot(d);
            if(d.charAt(d.length() - 1) == '0' && d.charAt(d.length() - 2) == '.') {
                d = d.substring(0, d.length() - 2);
                hasdot = false;
            }
            screen.setText(screen.getText() + d);
            moveCaret();
        }
    }

    public void dot(View v) {
        if(!hasdot && !operator
                && !TextUtils.isEmpty(screen.getText().toString())
                && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            screen.setText(screen.getText()+".");
            hasdot = true;
            moveCaret();
        }
    }

    public void clear(View v) {
        screen.setText("");
        hasdot = operator = false;
        moveCaret();
    }

    public void ce(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) && !operator) {
            getLastDigitedNumber();
            hasdot = false;
            operator = true;
            moveCaret();
        }
    }

    public void erase(View v) {
        char lastChar = ' ';
        if(!TextUtils.isEmpty(screen.getText().toString())) {
            if(screen.getText().toString().compareToIgnoreCase("Infinity") == 0) {
               screen.setText("");
            } else {
                if (operator) {
                    screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
                    operator = false;
                } else {
                    lastChar = screen.getText().charAt(screen.getText().length() - 1);
                    screen.setText(screen.getText().subSequence(0, screen.getText().length() - 1));
                }
                if (screen.getText().length() > 0) {
                    char currentLastOne = screen.getText().charAt(screen.getText().length() - 1);
                    if (currentLastOne == '.') {
                        screen.setText(screen.getText().subSequence(0, screen.getText().length() - 1));
                        hasdot = false;
                    } else if (currentLastOne == ' ') {
                        operator = true;
                    } else if (currentLastOne == '-') {

                        screen.setText(screen.getText().subSequence(0, screen.getText().length() - 1));
                    }
                }
            }

            if(lastChar == '.')
                hasdot = false;
            moveCaret();
        }
    }


    public void result(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            List<Double> number = new ArrayList<Double>();
            List<Character> operators = new ArrayList<Character>();

            String expression = screen.getText().toString();
            String value = "";
            expression+=" ";
            for(int i = 0; i < expression.length(); i++) {
                if((expression.charAt(i) != ' ' && !isAnOperatorSign(expression.charAt(i))) ||
                        (isAnOperatorSign(expression.charAt(i)) && expression.charAt(i + 1) != ' ')) {
                    value += expression.charAt(i);
                } else {
                    if(value != "") {
                        number.add(Double.parseDouble(value));
                        value = "";
                    } else {
                        if(isAnOperatorSign(expression.charAt(i))) {
                            operators.add((expression.charAt(i)));
                        }
                    }
                }
            }
            if(operators.size() > 0) {
                String resp = calculation(number, operators).toString();
                hasdot = doesItHasADot(resp);
                if(resp.charAt(resp.length() - 1) == '0' && resp.charAt(resp.length() - 2) == '.') {
                    resp = resp.substring(0, resp.length() - 2);
                    hasdot = false;
                }
                // update screen
                screen.setText(resp);
                operator = false;
                moveCaret();
            }
        }

    }

    public Double calculation(List<Double>number, List<Character>op) {
        Double resp = 0.0;
        while(number.size() > 1) {
            int i;
            boolean found = false;
            for (i = 0; i < op.size(); i++) {
                if(op.get(i) == '/' || op.get(i) == '*') {
                    found = true;
                    break;
                }
            }

            if(!found) {
                i = 0;
            }
            resp = doMath(number.get(i), number.get(i + 1), op.get(i));
            number.set(i + 1, resp);
            number.remove(i);
            op.remove(i);
        }

        return resp;
    }

    public String getLastDigitedNumber() {
        String expression = "";
        int i = 0;
        for(i = screen.getText().length() - 1; i >= 0; i--) {
            if(screen.getText().charAt(i) != ' ') {
                expression+=screen.getText().charAt(i);
            } else {
                break;
            }
        }
        screen.setText(screen.getText().subSequence(0, i + 1));
        String finalres = "";
        for(i = expression.length() - 1; i >= 0; i--)
            finalres+=expression.charAt(i);

        return finalres;
    }

    public boolean doesItHasADot(String s) {
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }

    public double doMath(Double n1, double n2, char op) {
        double r = 0;
        switch(op) {
            case '+':
                r = n1 + n2;
                break;
            case '-':
                r = n1 - n2;
                break;
            case '/':
                r = n1 / n2;
                break;
            case '*':
                r = n1 * n2;
                break;
        }
        return r;
    }

    public boolean isAnOperatorSign(char c) {
        return (c == '+' || c == '-' || c == '/' || c == '*');
    }

    public void moveCaret() {
        screen.setSelection(screen.getText().length());
    }

    public void displayResult(String result) {
        Toast.makeText(CalculatorActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
