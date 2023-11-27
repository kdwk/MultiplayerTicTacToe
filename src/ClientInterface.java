public interface ClientInterface {
    public void receive(ServerEvent event);
    public void send(ClientEvent event);
}
