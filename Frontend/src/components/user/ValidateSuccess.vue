<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <img src="../../assets/img/TJlogo.jpg" alt="同舟共济">
      </div>
      <el-form :label-position="labelPosition" label-width="60px" class="login_form" >
        <!-- 用户名 -->
        <el-form-item>
          <h2> 恭喜，您已经成功激活账户！</h2>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "ValidateSuccess",
  data(){
    return{
      labelPosition: 'right'
    }
  },
  created() {
    this.$axios
      .post('/activate',{
        emailToken: this.$route.query.token
      })
      .then(resp => {
        console.log(resp.data)
      })
      .catch(failResponse => {})
  },
  methods:{
    login(){
      this.$router.push('/home')
    }
  }
}
</script>

<style lang="less" scoped>
.login_container {
  background: url("../../assets/img/JiadingCampus.png") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}
.btns {
  display: flex;
  justify-content: flex-end;
}
.login_box {
  width: 450px;
  height: 300px;
  background-color: #dddddd;
  opacity: 0.85;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

.avatar_box {
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;

img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eee;
}

}
}

.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
</style>
