package su.dreamtime.dtbotsapi.bots.common;

import su.dreamtime.dtbotsapi.DTBotsAPI;
import su.dreamtime.dtbotsapi.commands.common.Command;
import su.dreamtime.dtbotsapi.commands.common.CommandListener;
import su.dreamtime.dtbotsapi.util.JsonParser;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BotClient extends Thread implements AutoCloseable {
    protected String remoteIp;
    protected int remotePort;
    protected String localIp;
    protected int localPort;
    protected Socket socket;
    private ReentrantLock messageLock = new ReentrantLock();
    private BufferedWriter out;
    private BufferedReader in;
    protected String name;

    private boolean close;

    public BotClient(String name, String remoteIp, int remotePort){
        this.name = name;
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    public final void create(){
        try {
            connect();
        } catch (Exception e) {
            DTBotsAPI.getLogger().warning("Cannot connect to remote server");
            e.printStackTrace();
        }
        start();
        DTBotsAPI.addClient(this);
    }

    /* Connection */

    private final void reconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            connect();
        } catch (Exception e) {
            socket = null;
            DTBotsAPI.getLogger().info("Cannot reconnect. Try again");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private final void connect() throws Exception {
        socket = new Socket(remoteIp, remotePort);
        socket.setSoTimeout(10000);
        this.localIp = socket.getLocalAddress().getHostName();
        this.localPort = socket.getLocalPort();
        DTBotsAPI.getLogger().info("this client " + this + " was connected to remote address " + remoteIp + ":" + remotePort);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sendRequest(new Command("CREATE_NAMED_CONNECTION", name));
        onConnect();
    }

    /* command and connection handler */

    @Override
    public final void run() {
        while (true) {
            try {
                messageLock.lock();
                String next;
                try {
                    if (!socket.isConnected()) {
                        reconnect();
                        continue;
                    }
                    next = in.readLine();
                    if (next == null) {
                        throw new NullPointerException();
                    }
                }
                catch (NullPointerException e) {
                    reconnect();
                    continue;
                } catch (SocketException e) {
                    if (e.getMessage().equalsIgnoreCase("Connection reset")) {
                        reconnect();
                    } else {
                        DTBotsAPI.getLogger().warning("BotClient. Cannot read input stream: " + e.getMessage());
                    }
                    continue;
                }
                Command cmd = JsonParser.parseJson(next, Command.class);

                if (cmd != null && cmd.name != null) {
                    CommandListener.execute(this, cmd);
                }

            } catch (Exception e) {
                if (e instanceof SocketTimeoutException) {

                } else {
                    if (e.getMessage().equalsIgnoreCase("Socket closed")) {
                        return;
                    }
                    e.printStackTrace();
                }
            } finally {
                messageLock.unlock();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public final void send(Command cmd) {
        try {
            messageLock.lock();
            cmd.setResponse(false);
            String out = JsonParser.toJson(cmd);

            try {
                this.out.write(out + "\n");
                this.out.flush();
            }
            catch (NullPointerException e){
                reconnect();
            }

        } catch (IOException e) {
            DTBotsAPI.getLogger().warning("BotClient. Cannot send command to remote server: " + e.getMessage());
        } finally {
            messageLock.unlock();
        }

    }

    public final Map<String, Object> sendRequest(Command cmd) {
        try {
            messageLock.lock();
            cmd.setResponse(true);
            String out = JsonParser.toJson(cmd);
            try {
                this.out.write(out + "\n");
                this.out.flush();
                String response = this.in.readLine();

                if (response == null) {
                    throw new NullPointerException();
                }
                return JsonParser.parseJson(response, new HashMap<String, Object>().getClass());
            }catch (NullPointerException e){
                reconnect();
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {

            }else {
                DTBotsAPI.getLogger().warning("BotClient. Cannot send request to remote server: " + e.getMessage());
            }
        } finally {
            messageLock.unlock();
        }
        return null;

    }

    public final void send(String command) {
        send(new Command(command, null));
    }

    public final Map<String, Object> sendRequest(String command) {
        return sendRequest(new Command(command, null));
    }

    /* Other owerriden and protected methods */

    @Override
    public final void close() {
        this.close(true);
    }

    public final void close(boolean removeClient) {
        try {
            this.onClose();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                this.stop();
                if (messageLock.isLocked()) {
                    messageLock.unlock();
                }
                if (removeClient) {
                    DTBotsAPI.removeClient(this);
                }
                DTBotsAPI.getLogger().info("this client " + this + " was closed!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException | IllegalMonitorStateException ignored) {

            }
        }
    }

    protected void onConnect(){

    }

    protected void onClose() {
    }

    /* getters and setters*/
    public final String getRemoteIp() {
        return remoteIp;
    }

    public final int getRemotePort() {
        return remotePort;
    }

    public final Socket getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotClient botClient = (BotClient) o;
        return localPort == botClient.localPort &&
                localIp.equals(botClient.localIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localIp, localPort);
    }

    /* to string */
    @Override
    public String toString() {
        return name + " " + localIp + ":" + localPort;
    }

}
