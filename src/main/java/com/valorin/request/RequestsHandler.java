package com.valorin.request;

import com.valorin.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class RequestsHandler {

    private final Set<Request> requests = new HashSet<>();
    private final Map<Request, BukkitTask> timers = new HashMap<>();
    private final Map<Request, Integer> times = new HashMap<>();

    public Request getRequest(String sender, String receiver) {
        for (Request request : requests) {
            if (request.getSender().equals(sender)
                    && request.getReceiver().equals(receiver)) {
                return request;
            }
        }
        return null;
    }

    public long getTime(String sender, String receiver) {
        if (getRequest(sender, receiver) == null) {
            return System.currentTimeMillis();
        }
        return times.get(getRequest(sender, receiver));
    }

    public void addRequest(String sender, String receiver, String arenaEditName) {
        Request request = new Request(sender, receiver, arenaEditName);
        requests.add(request);
        times.put(request, 0);
        BukkitTask timer = Bukkit.getScheduler().runTaskTimerAsynchronously(
                Main.getInstance(),
                () -> {
                    times.put(request, times.get(request) + 1);
                    if (times.get(request) == 60) {
                        if (getRequest(sender, receiver) != null) {
                            removeRequest(sender, receiver);
                            if (Bukkit.getPlayerExact(sender) != null) {
                                sm("&b你发送给{receiver}的请求长时间未得处理，已取消...",
                                        Bukkit.getPlayerExact(sender),
                                        "receiver", new String[]{receiver});
                            }
                        }
                        timers.get(request).cancel();
                    }
                }, 20, 20);
        timers.put(request, timer);
    }

    public void removeRequest(String sender, String receiver) {
        Request request = getRequest(sender, receiver);
        requests.remove(request);
        if (!timers.containsKey(request)) {
            return;
        }
        timers.get(request).cancel();
        timers.remove(request);
        if (!times.containsKey(request)) {
            return;
        }
        times.remove(request);
    }

    public List<String> getReceivers(String sender) {
        List<String> list = new ArrayList<>();
        for (Request request : requests) {
            if (request.getSender().equals(sender)) {
                list.add(request.getReceiver());
            }
        }
        return list;
    }

    public List<String> getSenders(String receiver) {
        List<String> list = new ArrayList<>();
        for (Request request : requests) {
            if (request.getReceiver().equals(receiver)) {
                list.add(request.getSender());
            }
        }
        return list;
    }

    /*
     * cause: 0:开赛了 1:下线了
     */
    public void clearRequests(String pn, int cause, String opponentName) {
        for (String sn : getSenders(pn)) {
            removeRequest(sn, pn);
            if (cause == 0) {
                if (!sn.equals(opponentName)) {
                    sm("&7玩家 &f{player} &7开始了别的比赛，之前未处理的请求已取消...",
                            Bukkit.getPlayerExact(sn), "player",
                            new String[]{pn});
                }
            }
            if (cause == 1)
                sm("&7玩家 &f{player} &7暂时下线了，之前未处理的请求已取消...",
                        Bukkit.getPlayerExact(sn), "player",
                        new String[]{pn});
        }
        for (String rn : getReceivers(pn)) {
            removeRequest(pn, rn);
            if (cause == 0) {
                if (!rn.equals(opponentName)) {
                    sm("&7之前向你发送单挑请求的玩家 &f{player} &7开始了别的比赛，请忽视之前的请求...",
                            Bukkit.getPlayerExact(rn), "player",
                            new String[]{pn});
                }
            }
            if (cause == 1)
                sm("&7之前向你发送单挑请求的玩家 &f{player} &7暂时下线了，请忽视之前的请求...",
                        Bukkit.getPlayerExact(rn), "player",
                        new String[]{pn});
        }
    }
}
