class MenuCodeisnotValidException extends Exception {
    private final String message;

    public MenuCodeisnotValidException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

