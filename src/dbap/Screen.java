package dbap;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Screen implements ActionListener {

	JFrame frame;
	JPanel pnl;

	//btn1=登録、2=更新、3=削除　4=表示
	JButton btn1, btn2, btn3, btn4;

	//label1=入力内容、2=表示内容
	JLabel label1, label2, label3;
	JTextField text;
	JTable table;
	JScrollPane sp;
	DefaultTableModel model;

	//エラーチェック用
	Validate validate = new Validate();
	int limitNum = 30;//制限文字数

	//DB接続
	private String url = "jdbc:mysql://localhost/database_ap?autoReconnect=true&useSSL=false";
	private String user = "kiya";
	private String pass = "skkh1455";

	public void Display() { //画面の作成

		frame = new JFrame();
		frame.setBounds(50, 50, 640, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("DBアクセスプログラム");
		frame.setVisible(true);

		pnl = new JPanel();
		pnl.setLayout(null);

		try {
			Connection connection = DriverManager.getConnection(url, user, pass);

			//テーブルの作成
			String[] header = { "PID", "内容" };
			//PIDに入力されている要素の数をcntとする
			String countNum = "SELECT COUNT(PID) cnt FROM TBL_HOGE";
			Statement statement = connection.createStatement();
			ResultSet count = statement.executeQuery(countNum);
			if(count.next()) {
				//要素の数をnumberとする
				int number = count.getInt("cnt");

				//現在登録されている要素の数でテーブルを作成する
				model = new DefaultTableModel(header, number);

				table = new JTable(model);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //複数行選択できないよう設定

				sp = new JScrollPane(table);
				sp.setBounds(10, 225, 580, 500);

				//カラムの幅を固定する
				TableColumn co = table.getColumnModel().getColumn(0);
				co.setMinWidth(100);
				co.setMaxWidth(100);

				Container con = frame.getContentPane();
				con.add(pnl);
				pnl.add(sp);

				//ボタンの作成
				btn1 = new JButton("登録");
				btn2 = new JButton("更新");
				btn3 = new JButton("削除");
				btn4 = new JButton("表示");

				btn1.addActionListener(this);
				btn2.addActionListener(this);
				btn3.addActionListener(this);
				btn4.addActionListener(this);

				btn1.setBounds(10, 20, 140, 75);
				btn2.setBounds(160, 20, 140, 75);
				btn3.setBounds(310, 20, 140, 75);
				btn4.setBounds(460, 20, 140, 75);

				btn1.setFont(new Font("MSゴシック", Font.BOLD, 20));
				btn2.setFont(new Font("MSゴシック", Font.BOLD, 20));
				btn3.setFont(new Font("MSゴシック", Font.BOLD, 20));
				btn4.setFont(new Font("MSゴシック", Font.BOLD, 20));

				pnl.add(btn1);
				pnl.add(btn2);
				pnl.add(btn3);
				pnl.add(btn4);

				label1 = new JLabel("入力内容");
				pnl.add(label1);
				label1.setBounds(10, 120, 200, 20);
				label1.setFont(new Font("MSゴシック", Font.BOLD, 20));

				label2 = new JLabel("※全角のみ使用可。全角30文字以内で入力してください。");
				pnl.add(label2);
				label2.setBounds(120, 120, 500, 20);
				label2.setFont(new Font("MSゴシック", Font.BOLD, 14));

				text = new JTextField();	//入力フィールド
				text.setText("");
				pnl.add(text);
				text.setBounds(10, 150, 580, 30);
				text.setFont(new Font("MSゴシック", Font.BOLD, 20));

				label3 = new JLabel("表示内容");
				label3.setBounds(10, 190, 100, 20);
				pnl.add(label3);
				label3.setFont(new Font("MSゴシック", Font.BOLD, 20));
			}
		} catch (Exception e) {
			System.out.println("画面作成：例外発生");
		}
	}

	public void errorMsg(int code) {

		String msg1 = "入力フィールドに文字列が入力されていません。入力フィールドに文字列を入力してください。";
		String msg2 = "入力フィールドに入力された文字数が" + limitNum + "文字を超えています";
		String msg3 = "入力フィールドに半角文字が含まれています。";
		String msg4 = "更新対象のデータが存在しません。";
		String msg5 = "表示対象のデータが存在しません。";

		if(code == 1000) {						//空白
			JOptionPane.showMessageDialog(frame, msg1, "エラーメッセージ_1000", JOptionPane.ERROR_MESSAGE);
		} else if(code == 1010) {				//文字数
			JOptionPane.showMessageDialog(frame, msg2, "エラーメッセージ_1010", JOptionPane.ERROR_MESSAGE);
		} else if(code == 1020) {				//半角
			JOptionPane.showMessageDialog(frame, msg3, "エラーメッセージ_1020", JOptionPane.ERROR_MESSAGE);
		} else if(code == 1030) {				//更新データ
			JOptionPane.showMessageDialog(frame, msg4, "エラーメッセージ_1030", JOptionPane.ERROR_MESSAGE);
		} else if(code == 1040) {				//表示データ
			JOptionPane.showMessageDialog(frame, msg5, "エラーメッセージ_1040", JOptionPane.ERROR_MESSAGE);
		}
	}

	//ボタンが押された時の動作
	@Override
	public void actionPerformed(ActionEvent e) {

		String str = e.getActionCommand();

		if(e.getSource() == btn1) {
			register(); //登録
		} else if(e.getSource() == btn2) {
			update(validate.code);	//更新
		} else if(e.getSource() == btn3) {
			delete(validate.code);	//削除
		} else if(e.getSource() == btn4) {
			print(validate.code);  //表示
		}
		text.setText(""); //テキストフィールドを空にする

	}

	public void register() { //登録

		try {

			Connection connection = DriverManager.getConnection(url, user, pass);

			//入力された値の取得
			String naiyou = text.getText();

			//空白チェック
			validate.nullCheck(naiyou);
			if(validate.code == 1000) {
				errorMsg(validate.code);
				connection.close();
			}

			//文字数チェック
			validate.mojicountCheck(naiyou, limitNum);
			if(validate.code == 1010) {
				errorMsg(validate.code);
				connection.close();
			}
			//半角文字チェック
			validate.HankakuCheck(naiyou);
			if(validate.code == 1020) {
				errorMsg(validate.code);
				connection.close();
			}

			//PIDに入力されている要素の数の最大値をmaxとする
			String getMax = "SELECT PID as max FROM TBL_HOGE WHERE PID=(SELECT MAX(PID)FROM TBL_HOGE)";
			Statement statement = connection.createStatement();
			ResultSet maxNum = statement.executeQuery(getMax);

			if(maxNum.next()) {
				int max = maxNum.getInt("max");

				String register = "INSERT INTO TBL_HOGE(PID,NAIYOU) VALUES(?,?)";
				PreparedStatement pstmt = connection.prepareStatement(register);

				String setNum = "";
				if(max < 9) {
					setNum = "00" + (max + 1);
				} else if(max < 99) {
					setNum = "0" + (max + 1);
				} else {
					setNum = Integer.toString(max + 1);
				}
				pstmt.setString(1, setNum);
				pstmt.setString(2, naiyou);
				int result = pstmt.executeUpdate();

				//登録した行をテーブルに追加・表示させる
				model.addRow(new Object[] { setNum, naiyou });

				connection.close();

			} else { //1番目の場合

				String register = "INSERT INTO TBL_HOGE(PID,NAIYOU) VALUES(?,?)";
				PreparedStatement pstmt = connection.prepareStatement(register);
				pstmt.setString(1, "001");
				pstmt.setString(2, naiyou);
				int result = pstmt.executeUpdate();

				//登録した行をテーブルに追加・表示させる
				model.addRow(new Object[] { "001", naiyou });

				connection.close();
			}

		} catch (Exception e) {
			System.out.println("「登録」例外発生");

		}

	}

	public void print(int code) { //表示

		try {

			//DB接続
			Connection connection = DriverManager.getConnection(url, user, pass);

			//PIDに入力されている要素の数をcntとする
			String countNum = "SELECT COUNT(PID) cnt FROM TBL_HOGE";
			Statement statement = connection.createStatement();
			ResultSet count = statement.executeQuery(countNum);
			if(count.next()) {

				//要素の数をnumとする
				int num = count.getInt("cnt");

				//表示対象チェック
				validate.emptyCheck(num);
				errorMsg(validate.code);

				String print = "SELECT PID,NAIYOU FROM TBL_HOGE";
				ResultSet pr = statement.executeQuery(print);
				if(pr.next()) {
					for(int i = 0; i < num; i++) {			//numまで繰り返しtableに表示する
						String pid = pr.getString("pid");
						String naiyou = pr.getString("naiyou");
						table.setValueAt(naiyou, i, 1);
						table.setValueAt(pid, i, 0);
						pr.next();		//次の行に移動

					}

					count.close();
					connection.close();

				}
			}
		} catch (Exception e) {
			System.out.println("「表示」例外発生");

		}
	}

	public void delete(int code) { //削除

		try {
			Connection connection = DriverManager.getConnection(url, user, pass);

			//選択したPID番号が存在するか確認
			String existCheck = "SELECT EXISTS (SELECT*FROM TBL_HOGE WHERE PID=?) ext";
			PreparedStatement pstmtE = connection.prepareStatement(existCheck);

			int row = table.getSelectedRow();
			if(row == -1) {		//どのセルも選択されていない場合のエラー処理
				errorMsg(1030);
			}
			String selectPID = table.getValueAt(row, 0).toString();//選択している行のPID番号

			pstmtE.setString(1, selectPID);
			ResultSet exist = pstmtE.executeQuery();
			if(exist.next()) {
				int checkNum = exist.getInt("ext");

				//更新対象チェック
				validate.updateCheck(checkNum);
				if(validate.code == 1030) {
					errorMsg(validate.code);
					model.removeRow(row); //存在しなかった行を削除
					connection.close();
				}

				String delete = "DELETE FROM TBL_HOGE WHERE PID=?";
				PreparedStatement pstmtD = connection.prepareStatement(delete);

				pstmtD.setString(1, selectPID);
				int result = pstmtD.executeUpdate();

				connection.close();
				model.removeRow(row);	//削除した行を削除する

			}
		} catch (Exception e) {
			System.out.println("「削除」例外発生");

		}
	}

	public void update(int code) { //更新

		try {
			Connection connection = DriverManager.getConnection(url, user, pass);

			//更新したPID番号が存在するか確認
			String existCheck = "SELECT EXISTS (SELECT*FROM TBL_HOGE WHERE PID=?) ext";
			PreparedStatement pstmtE = connection.prepareStatement(existCheck);

			int row = table.getSelectedRow(); //選択された行のインデックスを返す
			int col = table.getSelectedColumn();	//選択された列のインデックスを返す

			if(row == -1) {		//どのセルも選択されていない場合のエラー処理
				errorMsg(1030);
			}
			String selectPID = table.getValueAt(row, 0).toString();//編集後のセルの値のPIDを取得
			String select = table.getValueAt(row, col).toString();	//編集後のセルの値

			pstmtE.setString(1, selectPID);
			ResultSet exist = pstmtE.executeQuery();
			if(exist.next()) {
				int checkNum = exist.getInt("ext");

				//更新対象チェック
				validate.updateCheck(checkNum);
				if(validate.code == 1030) {
					errorMsg(validate.code);
					model.removeRow(row); //存在しなかった行を削除
					connection.close();
				}

				String update = "UPDATE TBL_HOGE SET NAIYOU=? WHERE PID=?";
				PreparedStatement pstmtU = connection.prepareStatement(update);

				//空白チェック
				validate.nullCheck(select);
				if(validate.code == 1000) {
					errorMsg(validate.code);
					connection.close();
				}

				//文字数チェック
				validate.mojicountCheck(select, limitNum);

				if(validate.code == 1010) {
					errorMsg(validate.code);
					connection.close();
				}
				//半角文字チェック
				validate.HankakuCheck(select);
				if(validate.code == 1020) {
					errorMsg(validate.code);
					connection.close();
				}

				pstmtU.setString(1, select);
				pstmtU.setString(2, selectPID);

				int result = pstmtU.executeUpdate();

				connection.close();
			}
		} catch (Exception e) {
			System.out.println("「更新」例外発生");

		}
	}
}
