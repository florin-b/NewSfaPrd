package my.logon.screen.enums;

public enum EnumTipUser {

	AV {
		public String getDescription() {
			return "Agent vanzari";
		}

		public String getTipAcces() {
			return "AV";
		}

		public int getCodAcces() {
			return 9;
		}
	},

	SD {
		public String getDescription() {
			return "Sef departament";
		}

		public String getTipAcces() {
			return "SD";
		}

		public int getCodAcces() {
			return 10;
		}
	},

	DV {
		public String getDescription() {
			return "Director vanzari";
		}

		public String getTipAcces() {
			return "DV";
		}

		public int getCodAcces() {
			return 12;
		}
	},

	DD {
		public String getDescription() {
			return "Director departament";
		}

		public String getTipAcces() {
			return "DV";
		}

		public int getCodAcces() {
			return 14;
		}
	},

	KA {
		public String getDescription() {
			return "Key account";
		}

		public String getTipAcces() {
			return "KA";
		}

		public int getCodAcces() {
			return 27;
		}
	},

	SK {
		public String getDescription() {
			return "Sef departament";
		}

		public String getTipAcces() {
			return "SK";
		}

		public int getCodAcces() {
			return 32;
		}
	},

	DK {
		public String getDescription() {
			return "Director key account";
		}

		public String getTipAcces() {
			return "DK";
		}

		public int getCodAcces() {
			return 35;
		}
	},

	CV {
		public String getDescription() {
			return "Consilier vanzari";
		}

		public String getTipAcces() {
			return "CV";
		}

		public int getCodAcces() {
			return 17;
		}
	},

	SM {
		public String getDescription() {
			return "Sef magazin";
		}

		public String getTipAcces() {
			return "SM";
		}

		public int getCodAcces() {
			return 18;
		}
	};

	public abstract String getDescription();

	public abstract String getTipAcces();

	public abstract int getCodAcces();

}
