public interface Client {
    public void receive(ServerEvent event);
    public void send(ClientEvent event);
}
