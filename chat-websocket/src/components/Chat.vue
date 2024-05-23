<template>
  <v-responsive
    class="align-centerfill-height mx-auto"
    max-width="900"
  >
    <div v-if="enter">
      <div class="mt-5 mb-4" v-if="currentTime!='-1'">
        当前时间：{{ dayjs(currentTime).format("YYYY-MM-DD HH:mm:ss") }}
        {{ currentTime }}
      </div>
      <div class="mb-4">
        <DatePicker v-model="date" mode="time" popover/>
        <v-btn text="修改时间" variant="flat" color="#07c160" class="ml-5 align-center mt-8"
               @click="onTimeChange"></v-btn>
      </div>
      <v-divider></v-divider>
      <v-text-field class="mt-5" label="消息" variant="outlined" hide-details
                    append-inner-icon="mdi-send" v-model="msg" @click:append-inner="onClick"></v-text-field>
      <v-container>
        <v-list>
          <v-list-item
            v-for="msg in messageList"
            :key="msg.id"
            :title="'用户 ' + msg.username"
            :subtitle=" '消息：'+msg.msg"
          ></v-list-item>
        </v-list>
      </v-container>
    </div>
    <div v-else>
      <v-sheet class="mx-auto mt-10" width="300">
        <div class="text-h5 align-center text-center mt-4 mb-7"><strong>请输入用户名</strong></div>
        <v-form ref="form">
          <v-text-field
            v-model="name"
            variant="outlined"
            density="compact"
            :counter="10"
            label="用户名"
            required
          ></v-text-field>

          <div class="d-flex flex-column">
            <v-btn
              class="mt-4"
              color="#07c160"
              block
              :disabled="name==''"
              @click="validate"
            >
              进入
            </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </v-responsive>
</template>

<script>
//
import dayjs from "dayjs";
import {DatePicker} from 'v-calendar';
import 'v-calendar/style.css';

export default {
  components: {DatePicker},
  data() {
    return {
      messageList: [],
      currentTime: 1716395571225,
      date: new Date(),
      name: '',
      enter: false,
      socket: undefined,
      msg: '',
    }
  },

  methods: {
    onClick() {
      if (this.msg == '' && this.name == '' && this.socket) {
        return;
      }
      if (this.socket == undefined || this.socket.readyState != WebSocket.OPEN) {
        return;
      }

      const data = {
        ms: -1,
        username: this.name,
        msg: this.msg,
      };

      this.socket.send(JSON.stringify(data));

    },

    onTimeChange() {
      if (this.socket == undefined) {
        return;
      }

      const timeChangeMsg = {
        ms: this.date.valueOf(),
      }
      this.socket.send(JSON.stringify(timeChangeMsg));
    },
    handleMsg(event) {
      const msg = JSON.parse(event.data);
      if (msg.ms > 0) {
        this.currentTime = msg.ms;
      } else {
        const show = {
          username: msg.username,
          id: msg.id,
          msg: msg.msg,
        }
        this.messageList.push(show);
      }

    },

    validate() {
      this.socket = new WebSocket("ws://localhost:8080");
      this.socket.addEventListener("open", function () {
      });

      this.socket.addEventListener("message", this.handleMsg)
      this.socket.addEventListener("close", function () {
        console.log("connection is closed");
      })

      this.socket.addEventListener("error", function (event) {
        console.log(event);
      })

      this.enter = true;

    }
  },
  computed: {
    dayjs() {
      return dayjs
    }
  }
}

</script>
