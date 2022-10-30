import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        StaticCache<String, String> cache = new StaticCache<>(2);

        // 设置缓存
        System.out.println("设置 a.txt缓存");
        cache.set("a.txt", new MyDataloader("./file/a.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        System.out.println("设置 b.txt缓存");
        cache.set("b.txt", new MyDataloader("./file/b.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        System.out.println("设置 c.txt缓存");
        cache.set("c.txt", new MyDataloader("./file/c.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        System.out.println("设置 d.txt缓存");
        cache.set("d.txt", new MyDataloader("./file/d.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        // 访问缓存
        System.out.println("获取 a.txt 内容");
        System.out.println("a.txt: " + cache.get("a.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        System.out.println("获取 b.txt 内容");
        System.out.println("b.txt: " + cache.get("b.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        System.out.println("获取 c.txt 内容");
        System.out.println("c.txt: " + cache.get("c.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");

        System.out.println("获取 d.txt 内容");
        System.out.println("d.txt: " + cache.get("d.txt"));
        System.out.println("展示 cache 缓存的 key:" + cache.keySet());
        System.out.println("");
    }
}

// 读取方法
class MyDataloader implements DataGetter<String> {
    private ReadFromFile reader;
    private final String path;

    public MyDataloader(String p) {
        reader = new ReadFromFile();
        path = p;
    }

    public String getValue() {
        return reader.readFileByLines(path);
    }
}

// 文件读取工具类
class ReadFromFile {
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder(); // 定义一个字符串缓存，将字符串存放缓存中
        String content = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        content = sb.toString();
        return content;
    }
}
