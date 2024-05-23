import WebSocket, {WebSocketServer} from 'ws'
import 'async-lock'
import AsyncLock from "async-lock";
import {v4 as uuidv4} from 'uuid';

const wss = new WebSocketServer({port: 8080});

let currentTime = (new Date()).valueOf();
console.log(currentTime);
const lock = new AsyncLock();
let id = 0;

wss.on('connection', function connection(ws) {
  console.log("connect event")
  ws.on('error', console.error)

  ws.on('message', function message(data) {
    const msg = JSON.parse(data);
    if (!'ms' in msg) {
      return;
    }
    if (msg.ms > 0) {
      lock.acquire("global", function () {
        currentTime = msg.ms;
      }, undefined, undefined);
    } else if ('username' in msg && 'msg' in msg) {
      msg.id = uuidv4();
      wss.clients.forEach(function (client) {
        if (client.readyState == WebSocket.OPEN) {
          client.send(JSON.stringify(msg));
        }
      })

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

