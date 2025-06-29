package MaslyakBank_Account.enums;


    public enum BinCode {
        PRIVAT_VISA("414949"),
        PRIVAT_MASTER("516875"),
        MONOBANK("537541");

        private final String value;

        BinCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

