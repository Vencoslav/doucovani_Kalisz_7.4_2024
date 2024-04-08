import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JTextField txtId;
    private JTextField txtNazev;
    private JCheckBox chckBoxPostaveno;
    private JTextField txtDatum;
    private JButton prevButton;
    private JButton nextButton;
    private JTable tabulka;
    private List<Budova> budovaList = new ArrayList<>();
    private int index = 0;
    private File selectedFile;

    public MainForm(){
        setContentPane(panelMain);
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMenu();
        prevButton.addActionListener(e->prev());
        nextButton.addActionListener(e->next());
        /*budovaList.add(new Budova(0, "akdjfaklsfjůas", true, LocalDate.of(2011,11,11)));
        display(getBudova(index));*/

        /* Pro Jirku a nikoho jiného </3 :´(
          ButtonGroup grp = new ButtonGroup();
          grp.add(nazevRadioButtonu1);
          grp.add(nazevRadioButtonu2);
          */
    }

    public void initMenu(){
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);

        JMenuItem chooseFile = new JMenuItem("vyber soubor");
        jmb.add(chooseFile);
        chooseFile.addActionListener(e-> chooseFile());

        JMenuItem addBudova = new JMenuItem("přidej budovu");
        jmb.add(addBudova);
        addBudova.addActionListener(e-> {
            createBudova();
            renderTable();
        });

        JMenuItem renderTable = new JMenuItem("vykresli tabulku");
        jmb.add(renderTable);
        renderTable.addActionListener(e->renderTable());
    }

    public void chooseFile(){
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("textové soubory", "txt"));
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){
            selectedFile = fc.getSelectedFile();
            readFile(selectedFile);
        }
    }

    public void readFile(File selectedFile){
        index = 0;
        budovaList.clear();
        try(Scanner sc = new Scanner(new BufferedReader(new FileReader(selectedFile)))){
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String nazev = parts[1];
                boolean hotovo = parts[2].equals("ano");
                LocalDate datumPostaveni = LocalDate.parse(parts[3]);
                budovaList.add(new Budova(id, nazev, hotovo, datumPostaveni));
                display(getBudova(index));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void prev(){
        if (index > 0){
            index--;
            display(getBudova(index));
        }
    }

    public void next(){
        if (index < budovaList.size()-1){
            index++;
            display(getBudova(index));
        }
    }

    public void display(Budova budova){
        txtId.setText(String.valueOf(budova.getId()));
        txtNazev.setText(budova.getNazev());
        chckBoxPostaveno.setSelected(budova.isHotovo());
        txtDatum.setText(String.valueOf(budova.getDatumPostaveni()));
    }

    public Budova getBudova(int i){
        return budovaList.get(i);
    }

    public void createBudova(){
        if (selectedFile == null){
            JOptionPane.showMessageDialog(this, "NEMÁŠ SOUBOR MAGORE");
            return;
        }
        int maxId = 0;
        for (Budova budova : budovaList){
            if(budova.getId() > maxId){}
            maxId = budova.getId();
        }
        int nextId = maxId + 1;

        JTextField idField = new JTextField(String.valueOf(nextId));
        JTextField nazevField = new JTextField();
        JCheckBox hotovoBox = new JCheckBox();
        JTextField datumField = new JTextField();

        Object[] fields = {
                "id: ", idField,
                "název: ", nazevField,
                "hotovo: ", hotovoBox,
                "datum postavení (yyyy-MM-dd)", datumField,
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "vložte informace: ", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
            int id = Integer.parseInt(idField.getText());
            String nazev = nazevField.getText();
            boolean hotovo = hotovoBox.isSelected();
            LocalDate datumPostaveni = LocalDate.parse(datumField.getText());
            Budova novaBudova = new Budova(id, nazev, hotovo, datumPostaveni);
            budovaList.add(novaBudova);
            writeIntoFile(selectedFile);
        }
    }

    public void writeIntoFile(File selectedFile){
        try (PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter(selectedFile)))){
            for (Budova b : budovaList){
                wr.print(b.getId() + ";" + b.getNazev() + ";" + (b.isHotovo() ? "ano" : "ne") + ";" + b.getDatumPostaveni() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void renderTable(){
        tabulka.setModel(new BuildingTableModel());
    }

    class BuildingTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return budovaList.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Budova currectBudova = budovaList.get(rowIndex);
            return switch(columnIndex){
                case 0 -> currectBudova.getId();
                case 1 -> currectBudova.getNazev();
                case 2 -> currectBudova.isHotovo();
                case 3 -> currectBudova.getDatumPostaveni();
                default -> null;
            };
        }
    }
}
