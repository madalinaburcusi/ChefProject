class MenuCodeisnotValidException extends Exception {
    private String message;
    public MenuCodeisnotValidException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

