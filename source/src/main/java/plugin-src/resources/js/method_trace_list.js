
MethodTracer = {
	            init : function(){
					var ds =new Ext.data.JsonStore({
							url: 'tracer/datas',
							root: 'traces',
							totalProperty: "totalCount",
    						fields:  [
    							{name: 'thread',mapping: 'thread'},
    							{name: 'isClosed',mapping: 'isClosed'},	
    							{name: 'args',mapping: 'args'},			
								{name: 'method', mapping: 'method'},
								{name: 'createTime', mapping: 'createTime'},
								{name: 'destoryTime', mapping: 'destoryTime'},
								{name: 'jdbcOpened', mapping: 'jdbcOpened'},
								{name: 'mcGet', mapping: 'mcGet'},
								{name: 'mcSet', mapping: 'mcSet'},
								{name: 'mcDel', mapping: 'mcDel'},
								{name: 'cost', mapping: 'cost',type: 'int'},
								{name: 'detail', mapping: 'detail'}
							],
							remoteSort: true
						});

						//ColumnModels
						var colModel =  new Ext.grid.ColumnModel([{ 
						   header: "线程",
						   dataIndex: 'thread',
						   width: 200,   
						   renderer: renderRed,
						   css: 'white-space:normal;'
						},{
						   header: "方法",
						   dataIndex: 'method',
						   renderer: renderRed,
						   width: 250
						},{
						   header: "开始时间",
						   dataIndex: 'createTime',
						   renderer: renderRed,
						   sortable: true,
						   width: 115 
						},{
						   header: "失效时间",
						   dataIndex: 'destoryTime',
						   renderer: renderRed,
						   width: 115 
						},{
						   header: "SQL执行条数",
						   dataIndex: 'jdbcOpened',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MC访问数",
						   dataIndex: 'mcTotal',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MC读取数",
						   dataIndex: 'mcGet',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MC写入数",
						   dataIndex: 'mcSet',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MC删除数",
						   dataIndex: 'mcDel',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "状态",
						   dataIndex: 'isClosed',
						   renderer: renderRed,
						   sortable: true,
						   width: 60
						},{
						   header: "耗时(ms)",
						   dataIndex: 'cost',
						   renderer: renderRed,
						   sortable: true,
						   width: 60
						}
						
						]);

						var grid = new Ext.grid.Grid('grid', {
							ds: ds,
							cm: colModel,
							loadMask: true
						});
						
						var layout=new InfoGridLayout({
										grid : grid,
										paging : true,
										pageSize: 25,
										displayMsg:'显示{0} - {1}条，共{2}条记录',
										emptyMsg: "暂无数据"
									});
						layout.render();
									
						var toolBar=layout.getToolBar(true);
						toolBar.add('-', {
					        pressed: true,
					        enableToggle:true,
					        text: '清除数据',
					        cls: 'x-btn button',
					        toggleHandler: clearDatas
					    });	
						
						layout.addDetailPanel('detail', {title:'轨迹跟踪',bindColumn:'detail',fitToFrame:true});
						layout.addDetailPanel('args', {title:'参数',bindColumn:'args',fitToFrame:true});
						
						function clearDatas(btn, pressed){
							var conn=new Ext.data.Connection({
							        method:'POST',
							        timeout:10000,
							        url:'tracer/clear'
							     });
							     
							conn.request({
							        callback:function(){
							        	ds.reload();
							        }
							        
							    });
				           
						}
	           }
	};

Ext.onReady(MethodTracer.init, MethodTracer, true);

function renderRed(value, cell, record,rowindex){
		var isClosed=record.get('isClosed');
		if(isClosed=='opened'){
			cell.attr='style="background:#ffcccc"';
		}
		return value;
}

