
HttpTracer = {
	            init : function(){
					var ds =new Ext.data.JsonStore({
							url: 'tracer/datas',
							root: 'traces',
							totalProperty: "totalCount",
    						fields:  [
    							{name: 'isClosed',mapping: 'isClosed'},
								{name: 'ip',mapping: 'ip'},
								{name: 'errors', mapping: 'errors'},
								{name: 'uri', mapping: 'uri'},
								{name: 'createTime', mapping: 'createTime'},
								{name: 'destoryTime', mapping: 'destoryTime'},
								{name: 'jdbcOpened', mapping: 'jdbcOpened'},
								{name: 'mcTotal', mapping: 'mcTotal'},
								{name: 'mcGet', mapping: 'mcGet'},
								{name: 'mcSet', mapping: 'mcSet'},
								{name: 'mcDel', mapping: 'mcDel'},
								{name: 'cost', mapping: 'cost',type: 'int'},
								{name: 'detail', mapping: 'detail'},
								{name: 'scoreGrade', mapping: 'scoreGrade'}
							],
							remoteSort: true
						});

						//ColumnModels
						var colModel =  new Ext.grid.ColumnModel([{ 
						   header: "Ip",
						   dataIndex: 'ip',
						   width: 115,   
						   renderer: renderRed,
						   css: 'white-space:normal;'
						},{
						   header: "URI",
						   dataIndex: 'uri',
						   renderer: renderRed,
						   width: 300
						},{
						   header: "��ʼʱ��",
						   dataIndex: 'createTime',
						   renderer: renderRed,
						   sortable: true,
						   width: 115 
						},{
						   header: "ʧЧʱ��",
						   dataIndex: 'destoryTime',
						   renderer: renderRed,
						   width: 115 
						},{
						   header: "SQLִ������",
						   dataIndex: 'jdbcOpened',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MC������",
						   dataIndex: 'mcTotal',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MC��ȡ��",
						   dataIndex: 'mcGet',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MCд����",
						   dataIndex: 'mcSet',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "MCɾ����",
						   dataIndex: 'mcDel',
						   renderer: renderRed,
						   sortable: true,
						   width: 80
						},{
						   header: "״̬",
						   dataIndex: 'isClosed',
						   renderer: renderRed,
						   sortable: true,
						   width: 60
						},{
						   header: "����",
						   dataIndex: 'errors',
						   renderer: renderRed,
						   sortable: true,
						   width: 60
						},{
						   header: "��ʱ(ms)",
						   dataIndex: 'cost',
						   renderer: renderRed,
						   sortable: true,
						   width: 60
						},{
						   header: "��������",
						   dataIndex: 'scoreGrade',
						   renderer: renderRed,
						   sortable: true,
						   width: 200
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
										displayMsg:'��ʾ{0} - {1}������{2}����¼',
										emptyMsg: "��������"
									});
						layout.render();
									
						var toolBar=layout.getToolBar(true);
						toolBar.add('-', {
					        pressed: true,
					        enableToggle:true,
					        text: '�������',
					        cls: 'x-btn button',
					        toggleHandler: clearDatas
					    });	
					    		
						layout.addDetailPanel('detail', {bindColumn:'detail',fitToFrame:true});
						
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

Ext.onReady(HttpTracer.init, HttpTracer, true);

function renderRed(value, cell, record,rowindex){
		var isClosed=record.get('isClosed');
		if(isClosed=='opened'){
			cell.attr='style="background:#ffcccc"';
		}
		return value;
}

