package equationmaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class EquationMaker {

    List<String> operadores = new ArrayList<>();
    List<String> sinais = new ArrayList<>();
    List<String> funcoes = new ArrayList<>();
    LinkedList<String> equacao = new LinkedList<>();
    LinkedHashSet<LinkedList> listatemporaria = new LinkedHashSet<>();
    int totalequations = 0;
    
    public static void main(String[] args) {
        EquationMaker equation = new EquationMaker();
        equation.startValores();
        equation.start();
    }
    
    public void start() {
        for(int i = 1; i < 4; i++){
            montaParenteses(i);
        }
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
            List<String> eqtemp = new ArrayList<>();
            adicionaSinais3(eqtemp, equacaofinal, 0, tamanho - 1);            
            for(LinkedList<String> equacaolocal : listatemporaria){
                p("************ ");
                imprimeEquacao(equacaolocal);
                List<String> eqtemp2 = new ArrayList<>();
                adicionaFuncoes(eqtemp2, equacaolocal, 0, tamanho - 1);
            }
            listatemporaria = new LinkedHashSet<>();
            
            pn("----------------------------------");
        }
    }
    
    public void adicionaFuncoes(List<String> eqfinal, List<String> eqoriginal, int posicao, int max) {
        if (posicao > max) {
            int pointer1 = 0;
            int pointer2 = 0;
            LinkedList<String> eqfinal2 = new LinkedList<>(eqoriginal);
            for (String sinal : eqoriginal) {
                if (sinal.equals("(")) {
                    eqfinal2.add(pointer2, eqfinal.get(pointer1));
                    pointer1++;
                    pointer2++;
                } 
                pointer2++;
            }
            totalequations++;
            p(totalequations+")    ");
            imprimeEquacao(eqfinal2);
        } else {
            for (String funcao : funcoes) {
                List<String> eqfinal2 = new ArrayList<>(eqfinal);
                eqfinal2.add(funcao);
                adicionaFuncoes(eqfinal2, eqoriginal, posicao + 1, max);
            }
        }
    }
    
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
            listatemporaria.add(eqfinal2);
        } else {
            for (String operador : operadores) {
                List<String> eqfinal2 = new ArrayList<>(eqfinal);
                eqfinal2.add(operador);
                adicionaOperadores(eqfinal2, eqoriginal, posicao + 1, max);
            }
        }
    }

    public void startValores() {
        sinais.add("+");
        sinais.add("-");

        operadores.add("+");
        operadores.add("-");
        operadores.add("*");
        operadores.add("/");
        operadores.add("^");
        
        funcoes.add("");
        funcoes.add("log"); // log2(8) = 2*2*2        
        funcoes.add("sqrt");
        funcoes.add("mod"); // modulo, remove negativo
        funcoes.add("logn"); //  e (Euler's Number)        
        funcoes.add("tan");
        funcoes.add("sin");
        funcoes.add("cos");
        funcoes.add("atan");
        funcoes.add("asin");
        funcoes.add("acos");
        funcoes.add("tanh");
        funcoes.add("sinh");
        funcoes.add("cosh");
        //Trigonometry (  cot, csc, sec, d2r, r2d, d2g, g2d, hyp) 
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
