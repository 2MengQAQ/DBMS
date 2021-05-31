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
	List<File_n> rlist = new ArrayList<File_n>();//��ȡ���ļ����ݵ��б�,�����ڿ���̨��ʾ
	List<File_n> wlist = new ArrayList<File_n>();//������file.db��д����ļ��б�
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		dbms.runSystem();//����ϵͳ
	}
	/**
	 * ����ϵͳ
	 * @throws IOException 
	 */
	public void runSystem() throws IOException{
		dbms.loadFileFromRom();//��10���ļ������ݶ�װ���ڴ�
		System.out.println("�ļ�f1-f10�Ѽ��ص��ڴ���");
		dbms.operateFile();
	}
	/**
	 * ������н�10���ļ������ݶ�װ�ص��ڴ���ȥ
	 * @throws IOException
	 */
	public void loadFileFromRom() throws IOException{
		BufferedReader bReader = null;
		for(int i=1;i<=10;i++){
			File_n file_n_r = new File_n();
			File_n file_n_w = new File_n();
			bReader = new BufferedReader(new FileReader("./file/f"+i+".txt"));
			String str_all_r = "";//һ���ļ�����������(��)
			String str_all_w = "";//һ���ļ�����������(д)
			String str_1;//ÿһ�е�����
			while((str_1 = bReader.readLine())!=null){
				str_all_r += str_1 + "\n";
				str_all_w += str_1 + "\r\n";
			}
			//ȥ�����һ�����з�
			file_n_r.setContent(str_all_r.substring(0, str_all_r.length()-1));
			rlist.add(file_n_r);
			file_n_w.setContent(str_all_w.substring(0, str_all_w.length()-2));
			wlist.add(file_n_w);
		}
		bReader.close();
	}
	/**
	 * �����ļ�
	 * @throws IOException 
	 */
	public void operateFile() throws IOException{
		boolean file_flag = true;//ѡ���ļ��õ���flag
		while(file_flag){
			System.out.println("ѡ���ļ�f1��f10��Ӧ����1��10���˳���Ӧ����0���������Ӧ������:");
			String f_str = sc.next();
			if(!f_str.equals("0")){//��ʾѡ�����f1-f10
				System.out.println("ѡ������ļ�f"+f_str);
				boolean oper_flag = true;
				while(oper_flag){
					System.out.println("�Ը��ļ��Ĳ�����0.�˳�  1.��ѯ  2.����  3.ɾ�����������Ӧ�����֣�");
					String o_str = sc.next();
					switch (o_str) {
					case "0":
						System.out.println("�˳��Ե�ǰ�ļ��Ĳ���");
						dbms.writefile(); //�˳�ʱ��f1-10������д��file.db��
						oper_flag = false;//ֻ�а�0��������ѭ��
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
						System.out.println("ֻ����������0-3�е�һ��");
						break;
					}
				}
			}else{//��ʾѡ���˳�
				System.out.println("���˳�ϵͳ");
				file_flag = false;
			}
		}
	}
	/**
	 * ���ڴ���10���ļ��������Լ��ڴ�ռ�����д��file.db��ȥ
	 * @throws IOException 
	 */
	public void writefile() throws IOException{
		FileWriter fWriter = new FileWriter(new File("./file/file.db"));
		fWriter.write("");//�Ƚ�ԭfile.db�е������ÿ�
 		PrintWriter pWriter;
		pWriter = new PrintWriter(new FileWriter("./file/file.db",true));//���в����ǵĲ�������
		for(int i=0;i<10;i++){
			pWriter.println("BEGIN_F"+(i+1));
			String content = wlist.get(i).getContent();
			if(content!=null&&!"".equals(content)){
				pWriter.println(content);
			}
			pWriter.println("�ļ�f"+(i+1)+"ռ�õ��ڴ棺"+content.length()+"�ֽ�");
			pWriter.println("END_F"+(i+1)+"\r\n");
		}
		fWriter.close();
		pWriter.close();
	}
	/**
	 * ��ѯ����
	 */
	public void select(int index){
		System.out.println("�ļ�f"+index+"�е�����Ϊ��");
		String content = rlist.get(index-1).getContent();//list���±��0��ʼ
		System.out.println(content);
	}
	/**
	 * ��������
	 */
	public void insert(int index){
		System.out.println("���������ļ�f"+index+"�в�������ݣ�");
		String i_sc = sc.next();
		if(i_sc==null||"".equals(i_sc)){
			System.out.println("���������Ϊ��");
		}
		String content_r = rlist.get(index-1).getContent();//ȡ�����ļ���ԭ�е�����
		if(content_r==null||"".equals(content_r)){//���ԭ���ǿյ�
			content_r = i_sc;//��ԭ�����ݵĻ����ϼ�������Ĳ���
		}else{
			content_r = content_r + "\n" + i_sc;//��ԭ�����ݵĻ����ϼ�������Ĳ���
		}
		String content_w = wlist.get(index-1).getContent();
		if(content_w==null||"".equals(content_w)){
			content_w = i_sc;
		}else{
			content_w = content_w + "\r\n" + i_sc;
		}
		dbms.update(index,content_r,content_w);//��������
	}
	/**
	 * ��������
	 */
	public void update(int index,String content_r,String content_w){
		System.out.println("�Ƿ���и��£���������ĸy��n���ֱ�����Ǻͷ񣬲����ִ�Сд��");
		boolean u_flag = true;
		while(u_flag){
			String u_sc = sc.next();
			if(u_sc.equals("y")||u_sc.equals("Y")){
				rlist.get(index-1).setContent(content_r);
				wlist.get(index-1).setContent(content_w);
				System.out.println("�Ѹ���");
				u_flag = false;
			}else if(u_sc.equals("n")||u_sc.equals("N")){	
				System.out.println("����ȡ��");
				u_flag = false;
			}else{
				System.out.println("��������ĸy��n");
			}
		}
	}
	/**
	 * ɾ������
	 */
	public void delete(int index){
		System.out.println("����ɾ���ļ�f"+index+"�е�����");
		String content_r = "";//��������Ϊ��
		String content_w = "";
	}
}
