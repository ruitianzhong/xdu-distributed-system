import WebSocket, {WebSocketServer} from 'ws'
import 'async-lock'
import AsyncLock from "async-lock";

const wss = new WebSocketServer({port: 8080});

let currentTime = (new Date()).valueOf();
console.log(currentTime);
var lock = new AsyncLock();

wss.on('connection', function connection(ws) {
  console.log("connect event")
  ws.on('error', console.error)

  ws.on('message', function message(data) {
    const msg = JSON.parse(data);
    if (msg.ms > 0) {
      lock.acquire("global", function () {
        currentTime = msg.ms;
      }, undefined, undefined);
    }
  })
})
console.log("set time interval");
setInterval(function () {
  wss.clients.forEach(function each(client) {
    if (client.readyState == WebSocket.OPEN) {
      const msg = {
        ms: currentTime, msg: '', user: '',
      }
      client.send(JSON.stringify(msg))

      lock.acquire("global", function () {
        currentTime += 1000;
      }, undefined, undefined)

    }
  })
}, 1000)

