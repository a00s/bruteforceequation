/*
  Made by Thiago Benazzi Maia to generate equations in a bruteforce mode

########### For MySQL ##########
 CREATE TABLE `equations` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `equation` varchar(255) NOT NULL,
   `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   UNIQUE KEY `equation` (`equation`)
 ) ENGINE=MyISAM DEFAULT CHARSET=latin1;
 */
package equationmaker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquationMaker {

    List<String> operadores = new ArrayList<>();
    List<String> sinais = new ArrayList<>();
    List<String> funcoes = new ArrayList<>();
    LinkedList<String> equacao = new LinkedList<>();
    LinkedHashSet<LinkedList> listatemporaria = new LinkedHashSet<>();
    int totalequations = 0;
    int totalequationscount = 0;
    Connection conn = null;
    Statement stmtfornecer;
    PreparedStatement sqlseguro;
    // -------------- CONFIG ----------------
    String mysql_user = "root";
    String mysql_pass = "password";
    String mysql_host = "127.0.0.1";
    String mysql_database = "fyequation";
    int equations_size_start = 1; // how many X the equation will start having
    int equations_size_end = 3; // how many X the equation will finish having

    public static void main(String[] args) {
        EquationMaker equation = new EquationMaker();
        equation.startValores();
        equation.start();
    }

    public void start() {
        conectar_mysql(mysql_database);
        for (int i = equations_size_start; i <= equations_size_end; i++) {
            montaParenteses(i);
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(EquationMaker.class.getName()).log(Level.SEVERE, null, ex);
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
            for (LinkedList<String> equacaolocal : listatemporaria) {
//                p("************ ");
//                imprimeEquacao(equacaolocal);
                List<String> eqtemp2 = new ArrayList<>();
                adicionaFuncoes(eqtemp2, equacaolocal, 0, tamanho - 1);
            }
            listatemporaria = new LinkedHashSet<>();

//            pn("----------------------------------");
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
//            totalequationscount++;
//            if(totalequationscount == 100000){
//            p(totalequations + ")    ");
//                 totalequationscount = 0;
//            imprimeEquacao(eqfinal2);
            saveEquacao(eqfinal2);
//            }            

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
//            imprimeEquacao(eqfinal2);      
            saveEquacao(eqfinal2);
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
        funcoes.add("sin");	// sine function
        funcoes.add("cos");	// cosine function
        funcoes.add("tan");	// tangens function
        funcoes.add("asin");	// arcus sine function
        funcoes.add("acos");	// arcus cosine function
        funcoes.add("atan");	// arcus tangens function
        funcoes.add("sinh");	// hyperbolic sine function
        funcoes.add("cosh");	// hyperbolic cosine
        funcoes.add("tanh");	// hyperbolic tangens function
        funcoes.add("asinh");	// hyperbolic arcus sine function
        funcoes.add("acosh");	// hyperbolic arcus tangens function
        funcoes.add("atanh");	// hyperbolic arcur tangens function
        funcoes.add("log2");	// logarithm to the base 2
        funcoes.add("log10");	// logarithm to the base 10
        funcoes.add("log");	// logarithm to base e (2.71828...)
        funcoes.add("ln");	// logarithm to base e (2.71828...)
        funcoes.add("exp");	// e raised to the power of x
        funcoes.add("sqrt");	// square root of a value
        funcoes.add("sign");	// sign function -1 if x<0; 1 if x>0

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

    public void saveEquacao(LinkedList<String> equacaol) {
        String eqf = "";
        for (String eq : equacaol) {
            eqf += eq;
        }
        pn(totalequations + ") " + eqf);
        String sql = "INSERT INTO equations SET equation = ?";
        try {
            sqlseguro = conn.prepareStatement(sql);
            sqlseguro.setString(1, eqf);
            sqlseguro.execute();
            sqlseguro.close();
        } catch (SQLException ex) {
            pn(ex.toString());
//            Logger.getLogger(EquationMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pn(String texto) {
        System.out.println(texto);
    }

    private void p(String texto) {
        System.out.print(texto);
    }

    public void conectar_mysql(String base) {
        try {
            String teste = "jdbc:mysql://" + mysql_host + ":3306/"+base+"?autoReconnect=true&";
            conn = DriverManager.getConnection(teste.toString(), mysql_user, mysql_pass);
            stmtfornecer = (Statement) conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            conn.setAutoCommit(true);
        } catch (Throwable erro) {
            System.out.println("Erro na conexao:" + erro);
        }
    }
}
