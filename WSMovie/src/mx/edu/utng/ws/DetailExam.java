package mx.edu.utng.ws;

public class DetailExam {
	private int id;
	private String cveMatter;
	private String cveContent;
	private String cveResult;
	private String cveQuestion;
	private String answer;
	private String examDepartament;
	private String reactive;
	
	public DetailExam(int id, String cveMatter, String cveContent, String cveResult, String cveQuestion, String answer,
			String examDepartament, String reactive) {
		super();
		this.id = id;
		this.cveMatter = cveMatter;
		this.cveContent = cveContent;
		this.cveResult = cveResult;
		this.cveQuestion = cveQuestion;
		this.answer = answer;
		this.examDepartament = examDepartament;
		this.reactive = reactive;
	}
	
	public DetailExam(){
		this(0, "", "", "", "", "", "", "");
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCveMatter() {
		return cveMatter;
	}

	public void setCveMatter(String cveMatter) {
		this.cveMatter = cveMatter;
	}

	public String getCveContent() {
		return cveContent;
	}

	public void setCveContent(String cveContent) {
		this.cveContent = cveContent;
	}

	public String getCveResult() {
		return cveResult;
	}

	public void setCveResult(String cveResult) {
		this.cveResult = cveResult;
	}

	public String getCveQuestion() {
		return cveQuestion;
	}

	public void setCveQuestion(String cveQuestion) {
		this.cveQuestion = cveQuestion;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getExamDepartament() {
		return examDepartament;
	}

	public void setExamDepartament(String examDepartament) {
		this.examDepartament = examDepartament;
	}

	public String getReactive() {
		return reactive;
	}

	public void setReactive(String reactive) {
		this.reactive = reactive;
	}

	@Override
	public String toString() {
		return "DetailExam [id=" + id + ", cveMatter=" + cveMatter + ", cveContent=" + cveContent + ", cveResult="
				+ cveResult + ", cveQuestion=" + cveQuestion + ", answer=" + answer + ", examDepartament="
				+ examDepartament + ", reactive=" + reactive + "]";
	}
	
	
	
	

}
