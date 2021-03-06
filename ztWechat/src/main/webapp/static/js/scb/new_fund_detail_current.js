$(document).ready(function () {
  $(function() {
      var Detail = {
          day1_profit_rate_arr: [],
          day7_profit_rate_arr: [],
          /**
           * 万份收益
           */
          showDay1Profit: function(profit, fix) {
              return (parseInt(profit || 0, 10)/10000).toFixed(fix || 4);
          },
          /**
           * 七日年化收益
           */
          showDay7Profit: function(profit, fix) {
              return (parseInt(profit || 0, 10)/1000000).toFixed(fix || 4);
          },
          bindEvents: function() {
              var _this = this;
              /*基金的七日年化和万份收益切换展示*/
              $('.chart-nav').on('click', 'a', function(event) {
                  event.preventDefault();
                  $('.chart-nav a').removeClass('on');
                  $(this).addClass('on');
                  var chart = $('#chart').highcharts();
                  $(".chart-tab a").removeClass('on').eq(0).addClass('on');
                  $('[data-profit_type="2"]').text("近七日年化(%)");
                  if ($(this).data('profit_type') == 1) {
                      chart.series[0].update({
                          name: '万份收益：',
                          data: _this['day1_profit_rate_arr'].slice(-7)
                      });
                      $("#messageBox").html("<div class=\" gray tc\">手指移至下方曲线图上，可查看近万份收益(元)</div>");
                  } else {
                      chart.series[0].update({
                          name: '七日年化：',
                          data: _this['day7_profit_rate_arr'].slice(-7)
                      });
                      $("#messageBox").html("<div class=\" gray tc\">手指移至下方曲线图上，可查看近七日年化(%)</div>");
                  }
              });

              // 数据图tab操作
              $(".chart-tab").on('click', 'a', function(event) {
                  event.preventDefault();
                  $(this).addClass('on').siblings('a').removeClass('on');
                  var duration = $(this).data('duration');
                  var type = $('.chart-nav .on').index();
                  var data_arr = [];
                  if (type == 1) {
                      $("#messageBox").html("<div class=\" gray tc\">手指移至下方曲线图上，可查看万份收益(元)</div>");
                      data_arr = duration == 7 ? _this['day1_profit_rate_arr'].slice(-7) : duration == 30 ? _this['day1_profit_rate_arr'].slice(-30) : _this['day1_profit_rate_arr'];
                  } else {
                      $("#messageBox").html("<div class=\" gray tc\">手指移至下方曲线图上，可查看近七日年化(%)</div>");
                      data_arr = duration == 7 ? _this['day7_profit_rate_arr'].slice(-7) : duration == 30 ? _this['day7_profit_rate_arr'].slice(-30) : _this['day7_profit_rate_arr'];
                  }
                  var chart = $('#chart').highcharts();
                  chart.series[0].update({
                      data: data_arr
                  });
              });
          },


         //  画出基金走势图
          initSPChart:
            function() {
              var _this = this;
              $.getJSON("scb/queryIncome", function(data) {
                          window._FUND_DATA = data;
                          var day1_profit_rate_arr = data['day1_profit_rate'];
                          var day7_profit_rate_arr = data['day7_profit_rate'];
                          var profit_rate_date_arr = data['profit_rate_date'];
                          for (var i = day1_profit_rate_arr.length - 1; i >= 0; i--) {
                              temp = parseInt(day1_profit_rate_arr[i], 10);
                              temp1 = parseInt(day7_profit_rate_arr[i], 10);
                              _this['day7_profit_rate_arr'].push([new Date(profit_rate_date_arr[i].substring(0, 4), parseInt(profit_rate_date_arr[i].substring(4, 6), 10) - 1, profit_rate_date_arr[i].substring(6, 8)).getTime(), temp1]);
                              _this['day1_profit_rate_arr'].push([new Date(profit_rate_date_arr[i].substring(0, 4), parseInt(profit_rate_date_arr[i].substring(4, 6), 10) - 1, profit_rate_date_arr[i].substring(6, 8)).getTime(), temp]);
                          };
                          $('#chart').highcharts('StockChart', {
                              chart: {
                                height:270,
                                pinchType: null,
                                panning:false,
//                                zoomType:x,
                                //制图区背景颜色
//                                  borderColor: "#fff6f3",
                                  borderRadius: 5,                                
                                  defaultSeriesType: "line",
                                  ignoreHiddenSeries: !0,
                                  marginTop:10,
                                  marginLeft:28,
//                                  margin:28,
//                                  spacing: [20, 10, 15, 10],
                                  backgroundColor: "#FFFFFF",
                                  //边框颜色
                                plotBorderColor: "#ffcbb6",
                                  resetZoomButton: {
                                      theme: {
                                          zIndex: 20
                                      },
//                                      position: {
//                                          align: "right",
//                                          x: -10,
//                                          y: 10
//                                      }
                                  }
                              },
                              credits: {
                                  enabled: !1
                              },
                              legend: {
                                  enabled: !1
                              },
                              navigator: {
                                  enabled: !1
                              },
                              scrollbar: {
                                  enabled: !1
                              },
                              rangeSelector: {
                                  enabled: !1
                              },
                              tooltip: {
//                                  shared: false,
                                  crosshairs: [{
                                      color: "#ffcbcc",
                                      width: 1
                                  }, {
                                      color: "#ffcbcc",
                                      width: 1
                                  }],
                                  formatter: function() {
                                      var r = "";
                                      //(new Date(this.x).getMonth()+1)+"-"+new Date(this.x).getDate()
                                      r = "<span class=\"gray fr\">" +_this.formatDate(new Date(this.x))+"</span>";
                                      $.each(this.points, function(i, point) {
                                          r += ("<span class=\"orange\">"+this.series.name);
                                          r += ((this.series.name == "万份收益：" ? _this.showDay1Profit(point.y) : _this.showDay7Profit(point.y))+"</span>");
                                      });
//                                      r += '</span><br>';
//                                      return r;
                                      $("#messageBox").html(r);
                                      return false;
                                  }
                              },
                              series: [{
                                  name: '七日年化：',
                                  data: _this['day7_profit_rate_arr'].slice(-7),
                                  type: 'areaspline',
                                  threshold: null,
//                                  tooltip: {
//                                      valueDecimals: 2
//                                  },
                                  fillColor: {
                                      linearGradient: {
                                          x1: 0,
                                          y1: 0,
                                          x2: 0,
                                          y2: 1
                                      },
                                      stops: [
                                          [0, Highcharts.getOptions().colors[0]],
                                          [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                                      ]
                                  }
                              }],
                              xAxis: {
          //                      tickInterval:1,
                                tickPixelInterval:70,
                                  ordinal: false,
                                  type: 'datetime',
                                  labels: {
                                      formatter: function() {
                                          return Highcharts.dateFormat("%m-%d", this.value);
                                      }
                                  }
                              },
                              yAxis: [{
                                opposite:false,
                                 min:0,
                                 offset: 13,
                                  labels: {
//                                    x:1,
//                                    y:,
                                    align: 'left',
                                      formatter: function() {
                                          var t = this.value >= 1000000 ? (this.value / 1000000).toFixed(1) + '%' : (this.value / 10000).toFixed(2); //大于等于7位数表示7日年化收益
                                          return '<span style="color:#606060">' + t + "</span>";
                                      }
                                  },
                                  showLastLabel: !0
                              }]
                          });

              });
          },
              
              
          initShow: function() {
              var _this = this;
              // set highchats 全局设置
              Highcharts.setOptions({
                colors:['#ffa07a'],
                  lang: {
                      rangeSelectorZoom: ""
                  }
              });
              

              this.initSPChart();
          },
          init: function() {
             this.initShow();
              this.bindEvents();
         },
          formatDate: function(date) {
              var m = date.getMonth() + 1;  
              m = m < 10 ? '0' + m : m;  
              var d = date.getDate();  
              d = d < 10 ? ('0' + d) : d;  
            return m + "-" + d; 
            },
          
      };

      Detail.init();
  });
});
