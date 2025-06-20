package tercero.com.base.controller.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import tercero.com.base.controller.DataStruc.List.LinkedList;


public class AdapterDao<T> implements InterfaceDao<T> {

    private final Class<T> clazz;
    private final Gson g;
    protected static String base_path = "data" + File.separatorChar;

    public AdapterDao(Class<T> clazz) {
        this.clazz = clazz;
        this.g = new Gson();
    }

    private String readFile() throws Exception {
        File file = new File(base_path + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            saveFIle("[]");
        }
        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(new FileReader(file))) {
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");

            }
        }
        return sb.toString();
    }

    private void saveFIle(String data) throws Exception {
        File file = new File(base_path + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            //  
            file.createNewFile();

        }
        //if (!file.exists()) {
        FileWriter fw = new FileWriter(file);
        fw.write(data);
        fw.flush();
        fw.close();
        //}


    }

    @Override
    public LinkedList<T> listAll() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'listAll'");
        LinkedList<T> lista = new LinkedList<>();
        try {
            String data = readFile();
            T[] m = (T[]) g.fromJson(data, java.lang.reflect.Array.newInstance(clazz, 0).getClass());
            lista.toList(m);
            //System.out.println(lista.getLength());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return lista;

    }

    @Override
    public void persist(T obj) throws Exception {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'persist'");
        LinkedList<T> list = listAll();
        list.add(obj);
        saveFIle(g.toJson(list.toArray()));

    }

    @Override
    public void update(T obj, Integer pos) throws Exception {
        LinkedList<T> list = listAll();
        list.update(obj, pos);
        saveFIle(g.toJson(list.toArray()));
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void update_by_id(T obj, Integer id) throws Exception {
        LinkedList<T> list = listAll();
        int inex = getIndex(id);
        list.update(obj, inex);
        saveFIle(g.toJson(list.toArray()));
    }

    @Override
    public T get(Integer id) throws Exception {
        if (id < 1 || id > listAll().getLength()) {
            throw new Exception("ID no valido");
        }
        try {
            LinkedList<T> list = listAll();
            T[] m = list.toArray();
            return m[getIndex(id)];
        } catch (Exception e) {
            throw new Exception("Error al obtener el objeto");
        }

    }

    private Integer getIndex(Integer id) throws Exception {
        //por busqueda binaria
        LinkedList<T> list = listAll();
        T[] m = list.toArray();
        int low = 0;
        int high = m.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Integer midId = (Integer) m[mid].getClass().getMethod("getId").invoke(m[mid]);
            if (midId.equals(id)) {
                return mid;
            } else if (midId < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        throw new Exception("ID no encontrado");
    }

}
