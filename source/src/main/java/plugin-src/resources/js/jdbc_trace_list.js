
HttpTracer = {
	            init : function(){
					var ds =new Ext.data.JsonStore({
							url: 'tracer/datas',
							root: 'traces',
							totalProperty: "totalCount",
    						fields:  [
    							{name: 'thread',mapping: 'thread'},
    							{name: 'isClosed',mapping: 'isClosed'},
								{name: 'createTime', mapping: 'createTime'},
								{name: 'destoryTime', mapping: 'destoryTime'},
								{name: 'cost', mapping: 'cost',type: 'int'},
								{name: 'detail', mapping: 'detail'},
								{name: 'sqls', mapping: 'sqls'},
								{name: 'count', mapping: 'count'},
								{name: 'sqlGrade', mapping: 'sqlGrade'}
							],
							remoteSort: true
						});
						
						//ColumnModels
						var colModel =  new Ext.grid.ColumnModel([{ 
						   header: "�߳�",
						   dataIndex: 'thread',
						   width: 220,   
						   renderer: renderRed,
						   css: 'white-space:normal;'
						},{
						   header: "��ʼʱ��",
						   dataIndex: 'createTime',
						   renderer: renderRed,
						   sortable: true,
						   width: 140 
						},{
						   header: "ʧЧʱ��",
						   dataIndex: 'destoryTime',
						   renderer: renderRed,
						   width: 140 
						},{
						   header: "״̬",
						   dataIndex: 'isClosed',
						   renderer: renderRed,
						   sortable: true,
						   width: 100
						},{
						   header: "��ʱ(ms)",
						   dataIndex: 'cost',
						   renderer: renderRed,
						   sortable: true,
						   width: 100
						},{
						   header: "���صļ�¼��(��)",
						   dataIndex: 'count',
						   renderer: renderRed,
						   width: 100
						},{
							header: "SQL�������",
							dataIndex: 'sqlGrade',
							renderer: renderRed,
							width: 140
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
					    		
						layout.addDetailPanel('detail', {title:'�켣����',bindColumn:'detail',fitToFrame:true});
						layout.addDetailPanel('sqls', {title:'SQL��ϸ',bindColumn:'sqls',fitToFrame:true});
						
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

