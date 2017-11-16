package dbap;

public class Validate {
	int code;

	//空白チェック
	public int nullCheck(String naiyou) {
		if(naiyou.length() == 0 || naiyou == null) {
			code = 1000;
		} else {
			code = 0;
		}
		return code;
	}

	//文字数チェック
	public int mojicountCheck(String naiyou, int limitNum) {
		int len = naiyou.length();
		if(len > limitNum) {
			code = 1010;
		} else {
			code = 0;
		}
		return code;

	}

	//半角文字チェック
	public int HankakuCheck(String naiyou) {

		char[] chars = naiyou.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if((c <= '\u007e') || // 英数字
					(c == '\u00a5') || // \記号
					(c == '\u203e') || // ~記号
					(c >= '\uff61' && c <= '\uff9f')) {
				code = 1020;
			} else {
				code = 0;
			}
		}
		return code;
	}

	//表示対象チェック
	public int emptyCheck(int num) {
		if(num < 1) {
			code = 1040;
		} else {
			code = 0;
		}
		return code;
	}

	//更新対象チェック

	public int updateCheck(int checkNum) {
		if(checkNum == 0) {
			code = 1030;
		} else {
			code = 0;
		}
		return code;
	}
}