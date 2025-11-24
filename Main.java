import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;

public class Main {

    // Interface que representa a DLL, usando JNA
    public interface ImpressoraDLL extends Library {
		// Caminho completo para a DLL

        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\Users\\susan\\Downloads\\Java-Aluno Graduacao\\Java-Aluno Graduacao\\E1_Impressora01.dll",
               ImpressoraDLL.class
        );

	    private static String lerArquivoComoString(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);
        int AvancaPapel(int linhas);
        int StatusImpressora(int param);
        int AbreGavetaElgin();
        int AbreGaveta(int pino, int ti, int tf);
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);
        int ModoPagina();
        int LimpaBufferModoPagina();
        int ImprimeModoPagina();
        int ModoPadrao();
        int PosicaoImpressaoHorizontal(int posicao);
        int PosicaoImpressaoVertical(int posicao);
        int ImprimeXMLSAT (String dados, int param);
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro;

    // Nova instancia da classe Scanner para usar posteriormente.
    private static final Scanner ler = new Scanner(System.in);

    // Instancia da ImpressoraDLL para utilizar no Menu.
    private static final ImpressoraDLL impressora = ImpressoraDLL.INSTANCE;


    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return ler.nextLine();
    }

    public static void configurarConexao() {

        System.out.println("Digite o tipo da impressora:");
        tipo = ler.nextInt();   //alterei os valores das variaveis globais p chamar dps
        ler.nextLine();

        System.out.println("Digite o parametro da impressora");
        parametro = ler.nextInt();
        ler.nextLine();

        System.out.println("Digite o modelo da impressora");
        modelo = ler.nextLine();

        System.out.println("Digite o tipo de conexão da impressora");
        conexao = ler.nextLine();
    }

    public static void abrirConexao() {

        int retorno = impressora.AbreConexaoImpressora(tipo,modelo,conexao,parametro);
        // Chamar funçao para abrir conexao no switch e imprimir o retorno com o tipo de erro

        if(retorno == 0){
            System.out.println("Conexão bem sucedida!");

            conexaoAberta = true;

        }
        else {
            System.out.println("Falha na conexão, erro: " + retorno);
        }
    }

    public static void fecharConexao() {

        // Comparando se a conexao esta aberta, encerrando e imprimindo

        if(conexaoAberta == true){
            impressora.ImpressaoTexto("Susan & Sidney", 1, 0, 36 );
            impressora.AvancaPapel(8);
            impressora.Corte(1);
            int retorno = impressora.FechaConexaoImpressora();
            if (retorno == 0){
                System.out.println("Conexão encerrada com sucesso");
            }
            else{
                System.out.println("Erro " + retorno);
            }
        }
        else{
            System.out.println("Não existe conexao");
        }

    }

    public static void SinalSonoro(){
        // verificando se a conexao esta aberta/ chamando a funcao comparando se o retorno e zero e se sim executando o som e imprimindo a mensagem
        if(conexaoAberta == true){
            int retorno = impressora.SinalSonoro( 4,5,5);
            if(retorno == 0){
                System.out.println("Sinal sonoro emitido com sucesso!");
            }else{
                System.out.println("Erro " + retorno);
            }
        }
        else{
            System.out.println("Favor abrir conexao antes de emitir o sinal sonoro");
        }
    }

    public static void AbrirGavetaElgin(){
        if(conexaoAberta == true) {

            int retorno = impressora.AbreGavetaElgin();
            if (retorno == 0) {
                System.out.println("Gaveta Elgin aberta");
            }else{
                System.out.println("Erro " + retorno);
            }
        }
        else {
            System.out.println("Favor abrir conexao antes de abrir a gaveta Elgin");
        }
    }

    public static void AbrirGaveta(){

        if(conexaoAberta == true) {

            int retorno = impressora.AbreGaveta(1, 5, 10);
            if (retorno == 0) {
                System.out.println("Gaveta aberta");
            }else{
                System.out.println("Erro " + retorno);
            }
        }
        else {
            System.out.println("Favor abrir conexao antes de abrir a gaveta");
        }

    }

    public static void imprimirTexto(){
        if(conexaoAberta == true){
            int retorno = impressora.ImpressaoTexto("Teste de impressão", 1, 4, 0);
            if (retorno >= 0) {
                System.out.println("Texto impresso com sucesso. Qtd de texto: " + retorno);
            }
            System.out.println(retorno);
        }
        else {
            System.out.println("Favor abrir conexao antes de imprimir o texto");
        }
    }

    public static void imprimirQrCode(){
        if (conexaoAberta == true){
            int retorno = impressora.ImpressaoQRCode("Teste de impressao", 6, 4);
            if(retorno == 0){
                System.out.println("Qr Code impresso com sucesso");
            }
            else{
                System.out.println("Erro " + retorno);
            }
        }
        else{
            System.out.println("Favor abrir conexao, antes de imprimir Qr Code");
        }
    }

    public static void imprimirCodigoDeBarras(){

        if(conexaoAberta == true){
            int retorno = impressora.ImpressaoCodigoBarras(8, "{A012345678912", 100, 2, 3);
            if(retorno == 0){
                System.out.println("Codigo de barras impresso com sucesso!");
            }
            else{
                System.out.println("Erro, " + retorno);
            }
        }
        else{
            System.out.println("Favor abrir conexao antes de imprimir codigo de barras");
        }

    }

    public static void imprimirXmlDeCancelamento(){
        if(conexaoAberta == true){
            int retorno = impressora.ImprimeXMLCancelamentoSAT("path=C:\\Users\\susan\\Downloads\\Java-Aluno Graduacao\\Java-Aluno Graduacao\\CANC_SAT.xml","Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==",0);
            if(retorno == 0){
                System.out.println("Cancelamento executado");
            }
            else{
                System.out.println("Erro " + retorno);
            }
        }
        else{
            System.out.println("Favor abrir conexao");
        }
    }

    public static void imprimirXmlDeSat(){

        if(conexaoAberta == true){
            int retorno = impressora.ImprimeXMLSAT("path=C:\\Users\\susan\\Downloads\\Java-Aluno Graduacao\\Java-Aluno Graduacao\\XMLSAT.xml", 0);
            if(retorno == 0){
                System.out.println("Cupom impresso com sucesso!");
            }
            else{
                System.out.println("Erro " + retorno);
            }
        }
        else{
            System.out.println("Favor abrir conexão");
        }

    }

    public static void main(String[] args) {

        boolean continuar = true;

        while (continuar) {
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA *******************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Canc SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10 - Sinal Sonoro");
            System.out.println("0  - Fechar Conexao e Sair");
            System.out.println("--------------------------------------");

            String escolha = capturarEntrada("\nDigite a opção desejada: ");

//            if (escolha.equals("0")) {
//                sair = false;
//            }
// criei com o switch para criar padrao

            switch (escolha) {

                case "0":
                    fecharConexao();
                    continuar = false;
                    break;


                case "1":// Chama a funcão para configurar a conexão atribuindo os valores as variaveis.
                    System.out.println("Vamos configurar sua conexão");
                    configurarConexao();
                    System.out.println("Conexão configurado com sucesso!");
                    break;


                case "2":
                    abrirConexao();
                    break;


                case "3":
                    imprimirTexto();
                    
                    impressora.LimpaBufferModoPagina();
                    impressora.AvancaPapel(3);
                    break;


                case "4":
                    imprimirQrCode();
                    impressora.Corte(3);
                    break;


                case "5":
                    imprimirCodigoDeBarras();
                    impressora.Corte(3);
                    break;

                    
                case "6":
						   imprimirXmlDeSat();
                           break;
                case "7":

                    imprimirXmlDeCancelamento();
                    break;

                case "8":
                    AbrirGavetaElgin();
                    break;
                    
                case "9":
                	AbrirGaveta();
                    break;
                    
                case "10":
                    SinalSonoro();
                    break;
                    
                default:
                    
            }
        }

        //scanner.close();
    }

}
