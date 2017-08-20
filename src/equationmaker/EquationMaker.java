package equationmaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import sun.security.util.Length;

public class EquationMaker {

    List<String> operadores = new ArrayList<>();
    List<String> sinais = new ArrayList<>();
    List<String> funcoes = new ArrayList<>();
    LinkedList<String> equacao = new LinkedList<>();
    LinkedHashSet<LinkedList> listatemporaria = new LinkedHashSet<>();

    public static void main(String[] args) {
        EquationMaker equation = new EquationMaker();
        equation.startValores();
//        equation.start();
//        equation.teste();
        equation.montaParenteses(3);
    }

    public void montaParenteses(int tamanho) {
        ArrayList<String> result = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        generate(tamanho, 0, 0, result, sb);
        for (String equacao : result) {
            List<String> equacao2 = new ArrayList<String>(Arrays.asList(equacao.split("")));
            int valor = 0;
            int posicao_final = 0;
            LinkedList<String> equacaofinal = new LinkedList(equacao2);
            List<Integer> posicoes = new ArrayList();
            for (int contador = 0; contador < 2 * tamanho; contador++) {
                int contador_local = 0;
                valor = 0;
                posicao_final = 0;
                segue:
                for (String equacao3 : equacao2) {
                    if (contador_local >= contador) {
                        if (equacao3.equals(")") && contador_local == contador) {
                            break segue;
                        }
                        if (equacao3.equals("(")) {
                            valor++;
                        } else if (equacao3.equals(")") && valor > 0) {
                            valor--;
                            if (valor == 0) {
                                int centro = ((posicao_final + contador + 1) / 2);
                                posicoes.add(centro);
                                break;
                            }
                        }
                    }
                    posicao_final++;
                    contador_local++;
                }
            }
            Collections.sort(posicoes);
            int contador = 0;
            for (Integer y : posicoes) {
                equacaofinal.add(y + contador, "x");
                contador++;
            }
            p("---- ");
            imprimeEquacao(equacaofinal);
            List<String> eqtemp = new ArrayList<>();
            adicionaSinais3(eqtemp, equacaofinal, 0, tamanho - 1);
            pn("----------------------------------");
        }
    }
    
//    public void limpaEquacao(List<String> eqfinal2){
//        for(int i = 1 ; i < eqfinal2.size() ; i++){
//            if(eqfinal2.get(i).equals("+") && eqfinal2.get(i-1).equals("(")){
//                pn("Aqui");
//                eqfinal2.remove(i);
//                i--;
//            }
//        }
//    }

    public void adicionaSinais3(List<String> eqfinal, List<String> eqoriginal, int posicao, int max) {
        if (posicao > max) {
            int pointer1 = 0;
            int pointer2 = 0;
            LinkedList<String> eqfinal2 = new LinkedList<>(eqoriginal);
            for (String sinal : eqoriginal) {
                if (sinal.equals("x")) {
                    eqfinal2.add(pointer2, eqfinal.get(pointer1));
                    pointer1++;
                    pointer2++;
                }
                pointer2++;
            }
            imprimeEquacao(eqfinal2);      
            List<String> eqtemp = new ArrayList<>();
            adicionaOperadores(eqtemp, eqfinal2, 0, max);
        } else {
            for (String sinal : sinais) {
                List<String> eqfinal2 = new ArrayList<>(eqfinal);
                eqfinal2.add(sinal);
                adicionaSinais3(eqfinal2, eqoriginal, posicao + 1, max);
            }
        }
    }

    public void adicionaOperadores(List<String> eqfinal, List<String> eqoriginal, int posicao, int max) {
        if (posicao > max) {
            int pointer1 = 0;
            int pointer2 = 0;
            String lastchar = "";
            LinkedList<String> eqfinal2 = new LinkedList<>(eqoriginal);
            for (String sinal : eqoriginal) {
                if (sinal.equals("(") && pointer2 > 0 && !lastchar.equals("(")) {
                    eqfinal2.add(pointer2, eqfinal.get(pointer1));
                    pointer1++;
                    pointer2++;
                } 
                lastchar = sinal;
                pointer2++;
            }
            imprimeEquacao(eqfinal2);
//            adicionaOperadores(eqtemp, eqfinal2, 0, max);
        } else {
            for (String operador : operadores) {
                List<String> eqfinal2 = new ArrayList<>(eqfinal);
                eqfinal2.add(operador);
                adicionaOperadores(eqfinal2, eqoriginal, posicao + 1, max);
            }
        }
    }

//    public void adicionaSinais(List<String> eqfinal, int posicao, int max) {
//        if (posicao > max) {
//
//            if (eqfinal.get(0).equals("+")) {
//                eqfinal.remove(0);
//            }
////            pn(eqfinal.toString());
//            adicionaOperadores(eqfinal, 1);
//        } else {
//            for (String sinal : sinais) {
//                List<String> eqfinal2 = new ArrayList<>(eqfinal);
//                eqfinal2.add(sinal);                
//                adicionaSinais(eqfinal2, posicao + 1, max);
//            }
//        }
//    }
//    public void teste() {
////        equacao = new LinkedList<>();
////        equacao.add("A");
////        equacao.add("B");
////        equacao.add(2, "C");
////        p(equacao.toString());         
//        List<String> listainicial = new ArrayList<>();
//        //adicionaSinais(listainicial, 1, 2); // funcionando
//    }

//    public void adicionaSinais2(List<String> eqfinal, int posicao, int max) {
//        if (posicao > max) {
//
//            if (eqfinal.get(0).equals("+")) {
//                eqfinal.remove(0);
//            }
//            pn(eqfinal.toString());
////            adicionaOperadores(eqfinal, 1);
//        } else {
//            for (String sinal : sinais) {
//                List<String> eqfinal2 = new ArrayList<>(eqfinal);
//                eqfinal2.add(sinal);
//                eqfinal2.add("x");
//                adicionaSinais2(eqfinal2, posicao + 1, max);
//            }
//        }
//    }
//    public void adicionaOperadores(List<String> eqfinal, int posicao) {
//        if (posicao == eqfinal.size()) {
//            pn(eqfinal.toString());
//        } else {
//            for (String oper : operadores) {
//                List<String> eqfinal2 = new ArrayList<>(eqfinal);
//                if (!oper.equals("")) {
//                    // validando
//                    if (!eqfinal2.get(posicao - 1).equals("+") && !eqfinal2.get(posicao - 1).equals("-")) {
//                        eqfinal2.add(posicao, oper);
//                        if (eqfinal2.get(posicao + 1).equals("+")) {
//                            eqfinal2.remove(posicao + 1);
//                        }
//                        adicionaOperadores(eqfinal2, posicao + 2);
//                    }
//                } else {
//                    adicionaOperadores(eqfinal2, posicao + 1);
//                }
//
////                pn("EF:"+eqfinal2.toString());
////                eqfinal2.add("x");
//            }
//        }
//    }

//    public void recuroper(String eqfinal, int posicao, int max) {
//        if (posicao > max) {
//            pn(eqfinal);
//        } else {
//            for (String oper : operadores) {
//                String eqfinal2 = eqfinal;
//                eqfinal2 += oper+"x";
//                recuroper(eqfinal2, posicao+1, max);
//            }
//        }
//    }
//    public String recuroper(String eqfinal, int max) {
//        if (eqfinal.length() == max) {
//            return eqfinal;
//        } else {
//            for (String oper : operadores) {
//                eqfinal += oper;
//                return recuroper(eqfinal, max);
//            }
//        }
//        return null;
//    }
    public void startFuncoes() {
//        funcoes.add("log"); // log2(8) = 2*2*2        
//        funcoes.add("sqrt");
//        funcoes.add("mod"); // modulo, remove negativo
//        funcoes.add("logn"); //  e (Euler's Number)        
//        funcoes.add("tan");
//        funcoes.add("sin");
//        funcoes.add("cos");
//        funcoes.add("atan");
//        funcoes.add("asin");
//        funcoes.add("acos");
//        funcoes.add("tanh");
//        funcoes.add("sinh");
//        funcoes.add("cosh");
        //Trigonometry (  cot, csc, sec, d2r, r2d, d2g, g2d, hyp) 
    }

    public void startValores() {
        sinais.add("+");
        sinais.add("-");

        operadores.add("");
        operadores.add("*");
        operadores.add("/");
        operadores.add("^");
    }

    public static void generate(int n, int left, int right, ArrayList<String> result, StringBuilder sb) {
        if (left < right) {
            return;
        }
        if (left == n && right == n) {
            result.add(sb.toString());
            return;
        }
        if (left == n) {
            generate(n, left, right + 1, result, sb.append(')'));
            sb.delete(sb.length() - 1, sb.length());
            return;
        }
        generate(n, left + 1, right, result, sb.append('('));
        sb.delete(sb.length() - 1, sb.length());
        generate(n, left, right + 1, result, sb.append(')'));
        sb.delete(sb.length() - 1, sb.length());
    }

    public void parenteses(LinkedList equacaol) {
        for (int i = 0; i < equacaol.size(); i++) {
            LinkedList equacao2 = new LinkedList<>(equacaol);
            pn(equacao2.toString());
        }

    }

    public void start() {
        int quant_inicial = 1;
        int quant_final = 3;
        for (int i = quant_inicial; i <= quant_final; i++) {
            equacao = new LinkedList<>();
            montaEquacao(i);
            parenteses(equacao);
//            recursiveP(equacao, 0, i + 1);
//            imprimeEquacao();
            pn("---------------------------------------------");
        }
    }

//    public void recursiveEquation(int no, int max){
//        if(no < max){            
//            recursiveEquation(no+1, max);
//        } else {
//            pn()
//        }
//    }
    public void recursiveP(LinkedList equacaol, int no, int max) {
        pn(no + ")" + equacaol.toString());
        if (no < max) {
            for (String oper : operadores) {
                equacaol.add(no, oper);
                recursiveP(equacaol, no + 2, max + 1);
            }
        } else {
            imprimeEquacao(equacaol);
            pn("");

        }
    }

    public void montaEquacao(int tamanho) {
        for (int i = 1; i <= tamanho; i++) {
            equacao.add("x");
        }
//        imprimeEquacao();
    }

    public void imprimeEquacao() {
        for (String eq : equacao) {
            p(eq);
        }
        pn("");
    }

    public void imprimeEquacao(LinkedList<String> equacaol) {
        for (String eq : equacaol) {
            p(eq);
        }
        pn("");
    }

    private void pn(String texto) {
        System.out.println(texto);
    }

    private void p(String texto) {
        System.out.print(texto);
    }
}
