package sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sms {
	// 添加学生
	public void add(Student stu) {
		try {
			Connection conn = null;
			PreparedStatement pstmt = null;
			try{
				conn = ConnectionFactory.getConn();
				String sql = "insert into t_student(name,age) values(?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, stu.getName());
				pstmt.setInt(2, stu.getAge());
				pstmt.execute();
			}finally{
				ConnectionFactory.close(null, pstmt, conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 */
	// 通过id删除学生 1002 1T "HELLO WORLD"
	public void deleteById(long id) {
		try {
			Connection conn = null;
			PreparedStatement pstmt = null;
			try{
				conn = ConnectionFactory.getConn();
				String sql = "delete from t_student where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, id);
				pstmt.executeUpdate();
			}finally{
				ConnectionFactory.close(null, pstmt, conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 通过id查询学生 ddl dml
	public Student queryById(long id) {
		Student stu = null;
		try {
			Connection conn =null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				conn = ConnectionFactory.getConn();
				String sql= "select * from t_student where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, id);
				rs = pstmt.executeQuery();
				while(rs.next()){
					String name = rs.getString("name");
					int age = rs.getInt("age");
					stu = new Student(id, name, age);
				}
			}finally{
				ConnectionFactory.close(rs, pstmt, conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stu;
	}

	// 查看所有学生信息
	public List<Student> queryAll() {
		List<Student> list = new ArrayList<Student>();
		try {
			Connection conn =null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				conn = ConnectionFactory.getConn();
				String sql= "select * from t_student";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()){
					long id = rs.getLong("id");
					String name = rs.getString("name");
					int age = rs.getInt("age");
					Student stu = new Student(id, name, age);
					list.add(stu);
				}
			}finally{
				ConnectionFactory.close(rs, pstmt, conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 更新
	 */
	public void update(Student newStu) {
		try {
			Connection conn = null;
			PreparedStatement pstmt= null;
			try{
				conn = ConnectionFactory.getConn();
				String sql = "update t_student set name=?,age=? where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newStu.getName());
				pstmt.setInt(2, newStu.getAge());
				pstmt.setLong(3, newStu.getId());
				pstmt.executeUpdate();		
			}finally{
				ConnectionFactory.close(null, pstmt, conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 菜单
	public void menu() {
		System.out.println("********学生管理系统*******");
		System.out.println("*1，查看所有学生信息*");
		System.out.println("*2，添加学生信息*");
		System.out.println("*3，删除学生信息*");
		System.out.println("*4，查询学生信息*");
		System.out.println("*5，修改学生信息*");
		System.out.println("*exit，退出*");
		System.out.println("*help，帮助*");
		System.out.println("***************************");
	}
	
	public static void main(String[] args) {
		// 创建sms对象
		Sms sms = new Sms();
		sms.menu(); // 显示主页面
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("请输入功能编号：");
				// 等待用户输入功能编号，等用户输入回车的时候获取回车前输入的内容
				String option = scanner.nextLine();
				switch (option) {
				case "1":// 查询所有
					System.out.println("以下是所有学生的信息：");
					List<Student> stus = sms.queryAll();
					for (Student stu : stus) {
						System.out.println(stu);
					}
					System.out.println("总计：" + stus.size() + " 人");
					break;
				case "2":// 添加
					while (true) {
						System.out.println("请输入学生信息【name#age】或者输入break回到上一级目录");
						String stuStr = scanner.nextLine();
						if (stuStr.equals("break")) {
							break;
						}
						String[] stuArr = stuStr.split("#");
						String name = stuArr[0];
						int age = Integer.parseInt(stuArr[1]);
						// 封装对象
						Student stu = new Student(null, name, age);
						sms.add(stu);
						System.out.println("添加成功！");
					}

					break;
				case "3":// 删除
					while (true) {
						System.out.print("请输入您要删除学生的id或break返回上一级目录:");
						String id = scanner.nextLine();
						if (id.equals("break")) {
							break;
						}
						sms.deleteById(Long.parseLong(id));
						System.out.println("删除成功！");
					}
					break;
				case "4":// 查询
					while (true) {
						System.out.print("请输入您要查询学生的id或break返回上一级目录:");
						String id = scanner.nextLine();
						if (id.equals("break")) {
							break;
						}
						Student stu = sms.queryById(Long.parseLong(id));
						System.out.println("以下是您要查找的学生的信息：");
						System.out.println(stu != null ? stu : "not found!");
					}
					break;
				case "5":// 修改
					while (true) {
						System.out.print("请输入您要修改学生的id或break返回上一级目录:");
						String id = scanner.nextLine();
						if (id.equals("break")) {
							break;
						}
						Student stu = sms.queryById(Long.parseLong(id));
						if (stu == null) {
							System.out.println("该学生不存在！");
							continue;
						}
						System.out.println("原信息为：" + stu);
						System.out.println("请输入您要修改的信息【name#age】");
						String stuStr = scanner.nextLine();
						String[] stuArr = stuStr.split("#");

						String name = stuArr[0];
						int age = Integer.parseInt(stuArr[1]);

						Student newStu = new Student(Long.parseLong(id), name, age);

						sms.update(newStu);
						System.out.println("修改成功！");
					}
					break;
				case "help":
					sms.menu();
					break;
				case "exit":
					System.out.println("bye bye");
					scanner.close();
					System.exit(0);
				default:
					System.out.println("输入出错，请重新输入！");
				}
				
			} catch (Exception e) {
				System.out.println("出错了！"+e.getMessage());
				continue;
			}
		}
	}
}
