package components;

public class Components {
		//id
		private String id;
		//代码版本
		private String CodeVersion;
		//部门
		private String Department;
		//开源组件名称
		private String componentName;
		//开源组件当前使用版本
		private String componentVersion;
		//开源组件最新版本
		private String componentLastestVersion;
		//开源组件官网
		private String componentWebsite;
		//组件存在漏洞
		private String vulnerablities;
		
		
		public String getID(){
			return id;
		}
		public void setID(String ID){
			this.id = ID;
		}
		
		public String getCodeVersion() {
			return CodeVersion;
		}

		public void setCodeVersion(String CodeVersion) {
			this.CodeVersion = CodeVersion;
		}
		
		public String getDepartment() {
			return Department;
		}

		public void setDepartment(String Department) {
			this.Department = Department;
		}
		
		public String getcomponentName() {
			return componentName;
		}

		public void setcomponentName(String componentName) {
			this.componentName = componentName;
		}
		
		public String getcomponentVersion() {
			return componentVersion;
		}

		public void setcomponentVersion(String componentVersion) {
			this.componentVersion = componentVersion;
		}
		
		public String getcomponentWebsite() {
			return componentWebsite;
		}

		public void setcomponentWebsite(String componentWebsite) {
			this.componentWebsite = componentWebsite;
		}
		
		public String getcomponentLastestVersion() {
			return componentLastestVersion;
		}

		public void setcomponentLastestVersion(String componentLastestVersion) {
			this.componentLastestVersion = componentLastestVersion;
		}
		
		public String getvulnerablities() {
			return vulnerablities;
		}

		public void setvulnerablities(String vulnerablities) {
			this.vulnerablities = vulnerablities;
		}
		
	}
