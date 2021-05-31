package dbms;
/**
 * create in 2021/5/21
 * @zhangxinyu
 * nuaa
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBMS {
	static DBMS dbms = new DBMS();
	List<File_n> rlist = new ArrayList<File_n>();//读取的文件内容的列表,用于在控制台显示
	List<File_n> wlist = new ArrayList<File_n>();//用于向file.db中写入的文件列表
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		dbms.runSystem();//运行系统
	}
	/**
	 * 运行系统
	 * @throws IOException 
	 */
	public void runSystem() throws IOException{
		dbms.loadFileFromRom();//将10个文件的内容都装入内存
		System.out.println("文件f1-f10已加载到内存中");
		dbms.operateFile();
	}
	/**
	 * 从外存中将10个文件的内容都装载到内存中去
	 * @throws IOException
	 */
	public void loadFileFromRom() throws IOException{
		BufferedReader bReader = null;
		for(int i=1;i<=10;i++){
			File_n file_n_r = new File_n();
			File_n file_n_w = new File_n();
			bReader = new BufferedReader(new FileReader("./file/f"+i+".txt"));
			String str_all_r = "";//一个文件的所有内容(读)
			String str_all_w = "";//一个文件的所有内容(写)
			String str_1;//每一行的内容
			while((str_1 = bReader.readLine())!=null){
				str_all_r += str_1 + "\n";
				str_all_w += str_1 + "\r\n";
			}
			//去掉最后一个换行符
			file_n_r.setContent(str_all_r.substring(0, str_all_r.length()-1));
			rlist.add(file_n_r);
			file_n_w.setContent(str_all_w.substring(0, str_all_w.length()-2));
			wlist.add(file_n_w);
		}
		bReader.close();
	}
	/**
	 * 操作文件
	 * @throws IOException 
	 */
	public void operateFile() throws IOException{
		boolean file_flag = true;//选择文件用到的flag
		while(file_flag){
			System.out.println("选择文件f1至f10对应数字1至10，退出对应数字0，请输入对应的数字:");
			String f_str = sc.next();
			if(!f_str.equals("0")){//表示选择的是f1-f10
				System.out.println("选择的是文件f"+f_str);
				boolean oper_flag = true;
				while(oper_flag){
					System.out.println("对该文件的操作：0.退出  1.查询  2.插入  3.删除，请输入对应的数字：");
					String o_str = sc.next();
					switch (o_str) {
					case "0":
						System.out.println("退出对当前文件的操作");
						dbms.writefile(); //退出时将f1-10的内容写到file.db中
						oper_flag = false;//只有按0才能跳出循环
						break;
					case "1":
						dbms.select(Integer.parseInt(f_str));
						break;
					case "2":
						dbms.insert(Integer.parseInt(f_str));
						break;
					case "3":
						dbms.delete(Integer.parseInt(f_str));
						break;
					default:
						System.out.println("只能输入数字0-3中的一个");
						break;
					}
				}
			}else{//表示选择退出
				System.out.println("已退出系统");
				file_flag = false;
			}
		}
	}
	/**
	 * 将内存中10个文件的内容以及内存占用情况写到file.db中去
	 * @throws IOException 
	 */
	public void writefile() throws IOException{
		FileWriter fWriter = new FileWriter(new File("./file/file.db"));
		fWriter.write("");//先将原file.db中的内容置空
 		PrintWriter pWriter;
		pWriter = new PrintWriter(new FileWriter("./file/file.db",true));//进行不覆盖的插入内容
		for(int i=0;i<10;i++){
			pWriter.println("BEGIN_F"+(i+1));
			String content = wlist.get(i).getContent();
			if(content!=null&&!"".equals(content)){
				pWriter.println(content);
			}
			pWriter.println("文件f"+(i+1)+"占用的内存："+content.length()+"字节");
			pWriter.println("END_F"+(i+1)+"\r\n");
		}
		fWriter.close();
		pWriter.close();
	}
	/**
	 * 查询内容
	 */
	public void select(int index){
		System.out.println("文件f"+index+"中的内容为：");
		String content = rlist.get(index-1).getContent();//list的下标从0开始
		System.out.println(content);
	}
	/**
	 * 插入内容
	 */
	public void insert(int index){
		System.out.println("请输入向文件f"+index+"中插入的内容：");
		String i_sc = sc.next();
		if(i_sc==null||"".equals(i_sc)){
			System.out.println("插入的内容为空");
		}
		String content_r = rlist.get(index-1).getContent();//取出该文件中原有的内容
		if(content_r==null||"".equals(content_r)){//如果原来是空的
			content_r = i_sc;//在原有内容的基础上加上输入的部分
		}else{
			content_r = content_r + "\n" + i_sc;//在原有内容的基础上加上输入的部分
		}
		String content_w = wlist.get(index-1).getContent();
		if(content_w==null||"".equals(content_w)){
			content_w = i_sc;
		}else{
			content_w = content_w + "\r\n" + i_sc;
		}
		dbms.update(index,content_r,content_w);//更新内容
	}
	/**
	 * 更新内容
	 */
	public void update(int index,String content_r,String content_w){
		System.out.println("是否进行更新？请输入字母y或n（分别代表是和否，不区分大小写）");
		boolean u_flag = true;
		while(u_flag){
			String u_sc = sc.next();
			if(u_sc.equals("y")||u_sc.equals("Y")){
				rlist.get(index-1).setContent(content_r);
				wlist.get(index-1).setContent(content_w);
				System.out.println("已更新");
				u_flag = false;
			}else if(u_sc.equals("n")||u_sc.equals("N")){	
				System.out.println("更新取消");
				u_flag = false;
			}else{
				System.out.println("请输入字母y或n");
			}
		}
	}
	/**
	 * 删除内容
	 */
	public void delete(int index){
		System.out.println("即将删除文件f"+index+"中的内容");
		String content_r = "";//将内容置为空
		String content_w = "";
	}
}
