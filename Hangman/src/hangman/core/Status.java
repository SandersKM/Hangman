package hangman.core;

public enum Status {
	WIN {
		@Override
		public boolean isOver() {
			return true;
		}
	},
	LOSS {
		@Override
		public boolean isOver() {
			return true;
		}
	},
	CONTINUE {
		@Override
		public boolean isOver() {
			return false;
		}
	};

	abstract public boolean isOver();
}
