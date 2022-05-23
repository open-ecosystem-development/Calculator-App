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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hmsecosystem.calculator.common.CipherUtil;
import com.hmsecosystem.calculator.common.IapApiCallback;
import com.hmsecosystem.calculator.common.IapRequestHelper;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//linda

import android.widget.TextView;
import com.huawei.agconnect.remoteconfig.AGConnectConfig;

//linda


public class MainActivity extends AppCompatActivity {

    private boolean adsFlag = false;
    private static final String HIDDEN_LEVEL_PRODUCTID = "RemoveAds1";
    private boolean isHiddenLevelPurchased = false;

    private BannerView defaultBannerView;
    private static final int REFRESH_TIME = 60;

    private InterstitialAd interstitialAd;

    private RewardAd rewardedAd;

    /*linda
    private static final String GREETING_KEY = "GREETING_KEY";
    private static final String SET_BOLD_KEY = "SET_BOLD_KEY";
    private AGConnectConfig config;
    private TextView textView;


    private void updateUI(){
        String text = config.getValueAsString(GREETING_KEY);
        Boolean isBold = config.getValueAsBoolean(SET_BOLD_KEY);
        textView.setText(text);
        View screenView = findViewById(R.id.view);

        if (isBold){
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            screenView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purpal));

        }else screenView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sky));
    }

    private void fetchAndApply(){
        config.fetch(0).addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply Network Config to Current Config
                config.apply(configValues);
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                textView.setText("fetch setting failed: " + e.getMessage());
            }
        });
    }
    linda*/

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Called when an ad is loaded successfully.
            showToast("Ad loaded.");
        }

        @Override
        public void onAdFailed(int errorCode) {
            // Called when an ad fails to be loaded.
            showToast(String.format(Locale.ROOT, "Ad failed to load with error code %d.", errorCode));
        }

        @Override
        public void onAdOpened() {
            // Called when an ad is opened.
            showToast(String.format("Ad opened "));
        }

        @Override
        public void onAdClicked() {
            // Called when a user taps an ad.
            showToast("Ad clicked");
        }

        @Override
        public void onAdLeave() {
            // Called when a user has left the app.
            showToast("Ad Leave");
        }

        @Override
        public void onAdClosed() {
            // Called when an ad is closed.
            showToast("Ad closed");
        }
    };

    private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0;
    private Button btPlus, btDiv, btMult, btMinus, btPlusMinus, btFat, btSqrt, btPow2, btDot;
    private Button btClear, btCe, btErase, btResult;
    private EditText screen;
    private  boolean operator, hasdot;

    private static final String TAG = "MainActivity";
    private String pushtoken = "";

    private IapClient mClient;
    // Define a variable for the Analytics Kit instance.
    HiAnalyticsInstance instance;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    //Remote Config private functions
    private static final String GREETING_KEY = "GREETING_KEY";
    private static final String SET_BOLD_KEY = "SET_BOLD_KEY";
    private AGConnectConfig config;
    private TextView textView;

    private void updateUI(){
        String text = config.getValueAsString(GREETING_KEY);
        Boolean isBold = config.getValueAsBoolean(SET_BOLD_KEY);
        textView.setText(text);
        View screenView = findViewById(R.id.view);

        if (isBold){
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            screenView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purpal));

        }else screenView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sky));
    }

    private void fetchAndApply(){
        config.fetch(0).addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply Network Config to Current Config
                config.apply(configValues);
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                textView.setText("fetch setting failed: " + e.getMessage());
            }
        });
    }
    //Remote Config setting done

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Analytics Kit logging.
        HiAnalyticsTools.enableLog();

        // Generate an Analytics Kit instance.
        instance = HiAnalytics.getInstance(this);
        mClient = Iap.getIapClient(this);

        // Associando variáveis aos views
        // Numbers
        bt0 = findViewById(R.id.bt0);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);
        bt8 = findViewById(R.id.bt8);
        bt9 = findViewById(R.id.bt9);

        // Math Operations
        btSqrt = findViewById(R.id.btsqrt);
        btPlus = findViewById(R.id.btplus);
        btMinus = findViewById(R.id.btminus);
        btDiv = findViewById(R.id.btdiv);
        btMult = findViewById(R.id.btmult);
        btPow2 = findViewById(R.id.btpow2);
        btFat = findViewById(R.id.btfat);

        // Other Operations
        btResult = findViewById(R.id.btresult);
        btDot = findViewById(R.id.btdot);
        btPlusMinus = findViewById(R.id.btplusminus);
        btClear = findViewById(R.id.btclear);
        btCe = findViewById(R.id.btCE);
        btErase = findViewById(R.id.btErase);

        //Remote config button
        textView = findViewById(R.id.greeting);
        Button button = findViewById(R.id.greeting);
        config = AGConnectConfig.getInstance();

        //config.applyDefault(R.xml.remote_config);
        textView.setText(config.getValueAsString(GREETING_KEY));

        View screenView = findViewById(R.id.view);
        fetchAndApply();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAndApply();
            }

        });
        //Remote config done



        // Screen
        screen = findViewById(R.id.screen);
        screen.setFocusable(false);

        HwAds.init(this);
        queryPurchases(null);
        getToken();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        queryPurchases(null);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart");
        queryPurchases(null);
    }

    // Metodos para cada um dos números
    public void add0(View v) {
        // Necessário por operator para falso, a qualquer momento o usuário
        // pode digitar um operador, já que ele está entrando com um número
        operator = false;
        // Se houver infinity na tela e o usuário digitar 0, remove tudo e põe o 0
        // evitar que o usuario digite também 000000000000000
        if(screen.getText().toString().compareToIgnoreCase("Infinity") == 0 ||
                (screen.getText().length() == 1 && screen.getText().charAt(0) == '0'))
            screen.setText("");
        screen.setText(screen.getText() + "0");
        movecarot();
    }

    public void add1(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infinity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0') ||
                screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "1");
        movecarot();
    }

    public void add2(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "2");
        movecarot();
    }

    public void add3(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "3");
        movecarot();
    }

    public void add4(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "4");
        movecarot();
    }

    public void add5(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "5");
        movecarot();
    }

    public void add6(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "6");
        movecarot();
    }

    public void add7(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "7");
        movecarot();
    }

    public void add8(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "8");
        movecarot();
    }

    public void add9(View v) {
        operator = false;
        // se existir apenas o 0 e for digitado outro número, remover o 0, pois não tem utilidade deixar ele lá
        // além disso, se houver infity também é para remover tudo
        if((screen.getText().length() == 1 && screen.getText().charAt(0) == '0')
                || screen.getText().toString().compareToIgnoreCase("Infinity") == 0)
            screen.setText("");
        screen.setText(screen.getText() + "9");
        movecarot();
    }

    // Operations
    public void sum(View v) {
        // se existe não exitir alguma expressão, então o computador não poderá incluir o operador de soma
        // Além disso, se existir algo, porém esse algo for Infinity, também não pode inserir o operador
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            // Se o operator for verdadeiro, é porque o usuário já inseriu um operador, e não é possivel ter um após o outro
            // Em concordancia com as calculadoras comuns, será permitido o ultimo operador inserido, por este
            if(operator) {
                // necessário remover os três ultimos caracteres, já que o formato da expressão ficou "espaco operador espaco"
                // então precisa-se remover esses três caracteres
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            // após a remoção inserir space + space...
            screen.setText(screen.getText().toString() + " + ");
            operator = true; // especificar que um operador foi inserido
            // foi adicionado um operador, então o proximo valor é garantido ser um número,
            // independentemente do usuario digitar outro operador ou não. Portanto é necessário por hasdot como false
            hasdot = false;
            movecarot();
        }
    }

    public void sub(View v) {
        // para inserir o -, mesmas operações realizadas para inserir o +
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            screen.setText(screen.getText() + " - ");
            operator = true;
            hasdot = false;
            movecarot();
        }
    }

    public void div(View v) {
        // para inserir o /, mesmas operações realizadas para inserir o +
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            screen.setText(screen.getText() + " / ");
            operator = true;
            hasdot = false;
            movecarot();
        }
    }

    public void mult(View v) {
        // para inserir o *, mesmas operações realizadas para inserir o +
        if(!TextUtils.isEmpty(screen.getText().toString()) && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            if(operator) {
                screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
            }

            screen.setText(screen.getText().toString() + " * ");
            operator = true;
            hasdot = false;
            movecarot();
        }
    }

    public void sqrt(View v) {
        // Para efetuar o calculo da raiz quadrada, necessário pegar o ultimo número
        // digitado e realizar a operacao somente neste e substitui-lo pelo resultado
        // se o ultimo valor for operador não é possivel calcular, mesma coisa se for infinity
        if(!TextUtils.isEmpty(screen.getText().toString()) &&
                !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            // obter ultimo valor digitado e remover da expressão
            String expression = getLastDigitedNumber();
            // realizar sqrt e converter para string
            String answer = Double.toString(Math.sqrt(Double.parseDouble(expression)));
            // Checar se o número tem um ponto, se não tiver, usuário pode inserir outro no numero obtido, caso contrario, não pode
            // não é permitido dois pontos por número (de acordo com calculadoras)
            hasdot = doesItHasADot(answer);
            // Se houver um ponto, mas o ultimo número na expressão for 0, significa que foi retornado um número inteiro
            // não um float, portanto, remover o . e o 0
            // Se ultimo caractere for 0 e penutimo for ., remover os dois
            if(answer.charAt(answer.length() - 1) == '0' && answer.charAt(answer.length() - 2) == '.') {
                answer = answer.substring(0, answer.length() - 2);
                // set hasdot para falso, já que foi removido
                hasdot = false;
            }
            screen.setText(screen.getText() + answer);
            movecarot();
        }
    }

    public void powto2(View v) {
        // mesmo processo efetuado para o sqrt, porém agora para realizar a potencia de 2
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            String expression = getLastDigitedNumber();
            // realiza o pow e obtem resposta como string
            String answer = Double.toString(Math.pow(Double.parseDouble(expression), 2));
            hasdot = doesItHasADot(answer);
            if(answer.charAt(answer.length() - 1) == '0' && answer.charAt(answer.length() - 2) == '.') {
                answer = answer.substring(0, answer.length() - 2);
                // set hasdot para falso, já que foi removido
                hasdot = false;
            }
            screen.setText(screen.getText() + answer);
            movecarot();
        }
    }

    public void fat(View v) {
        // Quase o mesmo processo que pow e sqrt, agora para fatorial
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            String expression = getLastDigitedNumber();
            // converter a expressão para double e depois para long (quero somente a parte inteira)
            long toInt = (long)Double.parseDouble(expression);
            // Essa solução consegue obter resposta até o fatorial 20, após isso, estoura a pilha
            if(toInt <= 20) {
                long answer = 1;
                for (int i = 2; i <= toInt; i++) {
                    answer *= i;
                }
                screen.setText(screen.getText() + Long.toString(answer));
            } else {
                // se maior que 20, só inclui o 0
                screen.setText(screen.getText() + "0");
            }
            // Não é necessário verificar se o resultado do fatorial tem ponto ou não,
            // porque é garantido que o valor retornado é um inteiro.
            // portanto hasdot é falso para permitir que o usuario insira outro ponto
            hasdot = false;
            movecarot();
        }
    }
    // Other operations
    public void plusminus(View v) {
        // inverter sinal se houver algum número
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            // obtem ultima expressão e remove
            String expression = getLastDigitedNumber();
            // converte para double e multiplica por -1
            String d = Double.toString(Double.parseDouble(expression) * -1);
            // atualizar hasdot para true ou false
            hasdot = doesItHasADot(d);
            // se houver ponto e o ultimo valor for 0 e o penultimo ., remove (número é inteiro)
            // necessário fazer isso, pois o double retorna valor com ponto flutuante.
            if(d.charAt(d.length() - 1) == '0' && d.charAt(d.length() - 2) == '.') {
                d = d.substring(0, d.length() - 2);
                // set hasdot para falso, já que foi removido
                hasdot = false;
            }
            screen.setText(screen.getText() + d);
            movecarot();
        }
    }

    public void dot(View v) {
        if(!hasdot && !operator
                && !TextUtils.isEmpty(screen.getText().toString())
                && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            screen.setText(screen.getText()+".");
            // hasdot true, para não permitir inserir outro
            hasdot = true;
            movecarot();
        }
    }

    public void clear(View v) {
        // apagar toda expressão da tela
        screen.setText("");
        // set hasdot e operator para false
        hasdot = operator = false;
        movecarot();
    }

    // o clear entry apaga o ultimo valor digitado ou obtido
    public void ce(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString()) && !operator) {
            // remover o ultimo valor com a funcao abaixo
            getLastDigitedNumber();
            // pode inserir ponto, ultimo valor foi removido
            hasdot = false;
            // não pode inserir operado (ou já existe um agora, ou não tem nada pois a expressão está vazia)
            operator = true;
            movecarot();
        }
    }

    public void erase(View v) {
        char lastChar = ' ';
        // só pode remover se a expressão não extiver vazia
        if(!TextUtils.isEmpty(screen.getText().toString())) {
            // primeiro checar se não é infinity
            if(screen.getText().toString().compareToIgnoreCase("Infinity") == 0) {
               // remove tudo se for
               screen.setText("");
            } else {
                // checar se ultmo valor não é operator, se for remover espaco operator espaco
                if (operator) {
                    screen.setText(screen.getText().subSequence(0, screen.getText().length() - 3));
                    operator = false; // allow the user insert another operator again, since last one was removed
                } else {
                    // obtendo ultimo charactere para checar mais tarde se o caracter removido não é um ponto
                    lastChar = screen.getText().charAt(screen.getText().length() - 1);
                    // remove ultimo char
                    screen.setText(screen.getText().subSequence(0, screen.getText().length() - 1));
                }
                // necessário checar qual o ultimo valor atual
                // se a expressão for maior que 0, há ainda algum dado (evitar IndexOutOfBoundsException)
                if (screen.getText().length() > 0) {
                    char currentLastOne = screen.getText().charAt(screen.getText().length() - 1);
                    // se tiver sobrado um ponto, remove junto com o número que foi tirado
                    if (currentLastOne == '.') {
                        screen.setText(screen.getText().subSequence(0, screen.getText().length() - 1));
                        // dot para false, para permitir que o usuário utilize novamente
                        hasdot = false;
                    } else if (currentLastOne == ' ') {
                        // se for espaco, significa que o que foi removido era um número e antes desse numero havia um operador
                        // portanto, operator = true, para especificar que já existe operador (antes era false, já que havia um número)
                        operator = true;
                    } else if (currentLastOne == '-') {
                        // Se for -, é porque o número removido era negativo, remove também
                        screen.setText(screen.getText().subSequence(0, screen.getText().length() - 1));
                    }
                }
            }

            // se o caracter removido não era nem operador, nem número, era um ., portanto é necessário setar hasdot para false
            // permitindo que o usuario entre com ponto novamente, já que o que tinha foi removido
            if(lastChar == '.')
                hasdot = false;
            movecarot();
        }
    }


    public void result(View v) {
        if(!TextUtils.isEmpty(screen.getText().toString())
                && !operator && screen.getText().toString().compareToIgnoreCase("Infinity") != 0) {
            List<Double> number = new ArrayList<Double>();
            List<Character> operators = new ArrayList<Character>();

            String expression = screen.getText().toString();
            String value = ""; // irá armazer numero por número
            expression+=" "; // para avaliar ultimo valor
            for(int i = 0; i < expression.length(); i++) {
                // Necessario checar se não existe espaco e o char em i não é operador ou
                // é operador (- negativo), porém não há espaco após este operador (há numero)
                if((expression.charAt(i) != ' ' && !isAnOperatorSign(expression.charAt(i))) ||
                        (isAnOperatorSign(expression.charAt(i)) && expression.charAt(i + 1) != ' ')) {
                    value += expression.charAt(i);
                } else {
                    // se chegar aqui, obteve um numero ou operador
                    if(value != "") {
                        // se diferente de vazio, só inserir na lista de números
                        number.add(Double.parseDouble(value));
                        value = ""; // empty novamente, para obter novos numeros
                    } else {
                        // se chegou aqui, pode ser operador (checar isso, já que pode ser também espaço)
                        if(isAnOperatorSign(expression.charAt(i))) {
                            // adicionar na lista de operadores
                            operators.add((expression.charAt(i)));
                        }
                    }
                }
            }
            // Checar se existe pelo menos um operador, caso contrario, não há operação para efetuar (portanto mantém o número)
            if(operators.size() > 0) {
                // obtem resposta como double e converte para string
                String resp = calculation(number, operators).toString();
                // se no resultado houver um ponto, atualizar isso
                hasdot = doesItHasADot(resp);
                // checar se dados restantes não são .0
                if(resp.charAt(resp.length() - 1) == '0' && resp.charAt(resp.length() - 2) == '.') {
                    // se entrou aqui, remover esse 0 e esse .
                    resp = resp.substring(0, resp.length() - 2);
                    // set hasdot para falso, já que este foi removido
                    hasdot = false;
                }
                // update screen
                screen.setText(resp);
                // set operator para falso de novo
                operator = false;
                movecarot();
            }
        }

    }

    // Extra functions
    public Double calculation(List<Double>number, List<Character>op) {
        Double resp = 0.0;
        // Enquanto o tamanho do vetor for maior do que um, é porque ainda há numero para calcular, e na posição 0 do
        // number, será armazenado o resultado da expressão
        while(number.size() > 1) {
            // primeiro checar se existe operadores com precendencia e suas posições
            int i;
            boolean found = false;
            for (i = 0; i < op.size(); i++) {
                if(op.get(i) == '/' || op.get(i) == '*') {
                    // encontrou operador com precedencia maior (da esquerda para direita)
                    found = true;
                    break;
                }
            }
            // se não tiver encontrado é porque não existe mais operador com precedencia maior, então começar do 0 em diante (só tem + e -)
            if(!found) {
                i = 0;
            }
            // a operação é feita entre o número i e i + 1 e armazenado em i + 1, quando foi pego os valores foi feito
            // numero, operador, numero, então é garantido que i = primeiro número operação,
            // i também refere-se ao operador e i + 1 refere=se ao número seguinte e também se chegou até aqui é garantido
            // que existe pelo menos dois números e um operador matemático
            resp = doMath(number.get(i), number.get(i + 1), op.get(i));
            // agora armazena o resultado em i + 1, e remove o i
            number.set(i + 1, resp);
            number.remove(i);
            // remove o operador utilizado também
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
                break; // encontrou espaco, ultimo número esta completo
            }
        }
        // A linha abaixo remove o ultimo valor
        // [Start, end) -> se i + 1 == 0, por i ser -1 (percorreu toda a expressao), não obtem nada (remove toda a expressao na tela),
        // provavelmente só havia 1 numero e ele foi removido
        // se i não for -1, havia operador (achou espaco), necessario manter-lo, para garantir formato (espaco operador espaco), i + 1 permite
        screen.setText(screen.getText().subSequence(0, i + 1));
        // necessario reverte-lo se houver (numero foi pego invertido)
        String finalres = "";
        for(i = expression.length() - 1; i >= 0; i--)
            finalres+=expression.charAt(i);

        return finalres;
    }

    // checar se há ponto no número ou não
    public boolean doesItHasADot(String s) {
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }

    // realizar matematica
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

    // Checar se o char é operador matemático
    public boolean isAnOperatorSign(char c) {
        return (c == '+' || c == '-' || c == '/' || c == '*');
    }

    // mover caret de acordo com o texto
    public void movecarot() {
        screen.setSelection(screen.getText().length());
    }

    private void loadDefaultBannerAd() {
        defaultBannerView = findViewById(R.id.hw_banner_view);
        defaultBannerView.setAdListener(adListener);
        defaultBannerView.setBannerRefresh(REFRESH_TIME);

        AdParam adParam = new AdParam.Builder().build();
        defaultBannerView.loadAd(adParam);
    }

    private void showToast(String message) {
//        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * get token
     */
    private void getToken() {
        Log.i(TAG, "get token: begin");

        // get token
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    pushtoken = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    if(!TextUtils.isEmpty(pushtoken)) {
                        Log.i(TAG, "get token:" + pushtoken);
//                        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                        myClip = ClipData.newPlainText("Push token", pushtoken);
//                        myClipboard.setPrimaryClip(myClip);
                        //showLog(pushtoken);
                    }
                } catch (Exception e) {
                    Log.i(TAG,"getToken failed, " + e);

                }
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if(!adsFlag){
            menu.removeItem(R.id.reward_ad);
            menu.removeItem(R.id.interstitial_ad);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reward_ad:
                rewardAdShow();
                return true;
            case R.id.interstitial_ad:
                loadInterstitialAd();
                return true;
            case R.id.remove_ads:
                intent = new Intent(MainActivity.this, NonConsumptionActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.privacy_policy:
                intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                return true;
            case R.id.terms_of_service:
                intent = new Intent(MainActivity.this, TermsOfServiceActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AdListener adListenerInterstitial = new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            showToast("Ad loaded");

            // Display an interstitial ad.
            showInterstitial();
        }

        @Override
        public void onAdFailed(int errorCode) {
            showToast("Ad load failed with error code: " + errorCode);
            Log.d(TAG, "Ad load failed with error code: " + errorCode);
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            showToast("Ad closed");
            Log.d(TAG, "onAdClosed");
        }

        @Override
        public void onAdClicked() {
            Log.d(TAG, "onAdClicked");
            super.onAdClicked();
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "onAdOpened");
            super.onAdOpened();
        }
    };

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId(getString(R.string.image_ad_id));
        interstitialAd.setAdListener(adListenerInterstitial);

        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    private void showInterstitial() {
        // Display an interstitial ad.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show(this);
        } else {
            showToast("Ad did not load");
        }
    }

    /**
     * Load a rewarded ad.
     */
    private void loadRewardAd() {
        if (rewardedAd == null) {
            rewardedAd = new RewardAd(this, getString(R.string.ad_id_reward));
        }

        RewardAdLoadListener rewardAdLoadListener = new RewardAdLoadListener() {
            @Override
            public void onRewardAdFailedToLoad(int errorCode) {
                showToast("onRewardAdFailedToLoad " + "errorCode is :" + errorCode);
            }

            @Override
            public void onRewardedLoaded() {
                showToast("onRewardedLoaded");
            }
        };

        rewardedAd.loadAd(new AdParam.Builder().build(), rewardAdLoadListener);
    }

    /**
     * Display a rewarded ad.
     */
    private void rewardAdShow() {
        if (rewardedAd.isLoaded()) {
            rewardedAd.show(this, new RewardAdStatusListener() {
                @Override
                public void onRewardAdClosed() {
                    showToast("onRewardAdClosed");

                    loadRewardAd();
                }

                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    showToast("onRewardAdFailedToShow " + "errorCode is :" + errorCode);
                }

                @Override
                public void onRewardAdOpened() {
                    showToast("onRewardAdOpened");
                }

                @Override
                public void onRewarded(Reward reward) {
                    // You are advised to grant a reward immediately and at the same time, check whether the reward
                    // takes effect on the server. If no reward information is configured, grant a reward based on the
                    // actual scenario.
//                    int addScore = reward.getAmount() == 0 ? defaultScore : reward.getAmount();
//                    showToast("Watch video show finished , add " + addScore + " scores");
//                    score += addScore;
//                    setScore(score);
//                    loadRewardAd();
                }
            });
        }
    }

    private void queryPurchases(final String continuationToken) {
        // Query users' purchased non-consumable products.
        IapRequestHelper.obtainOwnedPurchases(mClient, IapClient.PriceType.IN_APP_NONCONSUMABLE, continuationToken, new IapApiCallback<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                Log.i(TAG, "@@@: obtainOwnedPurchases, success, result: " + result);
                checkHiddenLevelPurchaseState(result);
                if (result != null && !TextUtils.isEmpty(result.getContinuationToken())) {
                    queryPurchases(result.getContinuationToken());
                }
            }

            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "@@@: obtainOwnedPurchases, type=" + IapClient.PriceType.IN_APP_NONCONSUMABLE + ", " + e.getMessage());
//                Toast.makeText(MainActivity.this, "get Purchases fail, " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkHiddenLevelPurchaseState(OwnedPurchasesResult result) {
        if (result == null || result.getInAppPurchaseDataList() == null) {
            Log.i(TAG, "@@@: result is null");
//            queryProducts();
            return;
        }
        Log.i(TAG, "@@@: result is " + result);
        List<String> inAppPurchaseDataList = result.getInAppPurchaseDataList();
        List<String> inAppSignature= result.getInAppSignature();
        Log.i(TAG, "@@@: inAppPurchaseDataList is " + inAppPurchaseDataList);
        Log.i(TAG, "@@@: inAppSignature is " + inAppSignature);
        for (int i = 0; i < inAppPurchaseDataList.size(); i++) {
            // Check whether the signature of the purchase data is valid.
            if (CipherUtil.doCheck(inAppPurchaseDataList.get(i), inAppSignature.get(i), CipherUtil.getPublicKey())) {
                try {
                    InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseDataList.get(i));
                    if (inAppPurchaseDataBean.getPurchaseState() == InAppPurchaseData.PurchaseState.PURCHASED) {
                        // Check whether the purchased product is Hidden Level.
                        if (HIDDEN_LEVEL_PRODUCTID.equals(inAppPurchaseDataBean.getProductId())) {
                            isHiddenLevelPurchased = true;
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "@@@: delivery:" + e.getMessage());
                }
            } else {
                Log.e(TAG, "@@@: delivery:" +  ", verify signature error");
            }
        }
        if (isHiddenLevelPurchased) {
            // Deliver the product after the Hidden Level was purchased.
        } else {
            // The user has not purchased the hidden level.
            // Obtain the product details and show the purchase entry to the user.
            showAds();
        }
    }

    private void showAds(){
        adsFlag=true;
        loadDefaultBannerAd();
        loadRewardAd();
    }

}
